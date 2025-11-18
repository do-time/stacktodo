import React, { useMemo, useState, useRef } from "react";
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  SafeAreaView,
  Modal,
  TextInput,
  Switch,
  Platform,
  Animated,
  PanResponder,
} from "react-native";
import Slider from "@react-native-community/slider";

const CATEGORIES = ["전체", "여행", "루틴", "업무", "기타"] as const;
const TASK_CATEGORIES: CategoryFilter[] = ["여행", "루틴", "업무", "기타"];

const TABS = ["today", "calendar", "routine"] as const;

type Tab = (typeof TABS)[number];
type CategoryFilter = (typeof CATEGORIES)[number];
type TaskStatus = "TODO" | "DONE";

type Task = {
  id: number;
  title: string;
  category: CategoryFilter;
  hasTime: boolean;
  time?: string;
  status: TaskStatus;
  progressPercent: number;
  pinned?: boolean;
  date: string; // YYYY-MM-DD
  sortOrder: number;
};

type RoutineGroup = {
  id: number;
  name: string;
  category: "루틴" | "여행";
  description?: string;
  tasks: string[];
};

const CATEGORY_STYLES: Record<
  CategoryFilter,
  {
    pillBg: string;
    pillText: string;
    pillBorder: string;
    accent: string;
  }
> = {
  전체: {
    pillBg: "#E2E8F0",
    pillText: "#475569",
    pillBorder: "#CBD5F5",
    accent: "#64748B",
  },
  여행: {
    pillBg: "#ECFDF3",
    pillText: "#047857",
    pillBorder: "#BBF7D0",
    accent: "#10B981",
  },
  루틴: {
    pillBg: "#EEF2FF",
    pillText: "#4F46E5",
    pillBorder: "#C7D2FE",
    accent: "#6366F1",
  },
  업무: {
    pillBg: "#FEF3C7",
    pillText: "#92400E",
    pillBorder: "#FDE68A",
    accent: "#F59E0B",
  },
  기타: {
    pillBg: "#E2E8F0",
    pillText: "#334155",
    pillBorder: "#CBD5F5",
    accent: "#64748B",
  },
};

// ---------- 날짜/캘린더 유틸 ----------

function toISODate(year: number, month: number, day: number) {
  const m = String(month).padStart(2, "0");
  const d = String(day).padStart(2, "0");
  return `${year}-${m}-${d}`;
}

function formatDateLabel(iso: string) {
  const [y, m, d] = iso.split("-");
  return `${y}.${m}.${d}`;
}

const nowGlobal = new Date();
const TODAY_ISO = toISODate(
  nowGlobal.getFullYear(),
  nowGlobal.getMonth() + 1,
  nowGlobal.getDate()
);

const initialTasks: Task[] = [
  {
    id: 1,
    title: "제주 여행 플랜 짜기",
    category: "여행",
    hasTime: true,
    time: "09:00",
    status: "TODO",
    progressPercent: 20,
    pinned: true,
    date: TODAY_ISO,
    sortOrder: 0,
  },
  {
    id: 2,
    title: "미라클 모닝 루틴",
    category: "루틴",
    hasTime: false,
    status: "TODO",
    progressPercent: 60,
    date: TODAY_ISO,
    sortOrder: 1,
  },
  {
    id: 3,
    title: "업무 정리 및 메일 확인",
    category: "업무",
    hasTime: true,
    time: "14:00",
    status: "TODO",
    progressPercent: 0,
    date: TODAY_ISO,
    sortOrder: 2,
  },
  {
    id: 4,
    title: "운동하기 (하체)",
    category: "루틴",
    hasTime: true,
    time: "19:30",
    status: "TODO",
    progressPercent: 10,
    date: TODAY_ISO,
    sortOrder: 3,
  },
];

const initialRoutineGroups: RoutineGroup[] = [
  {
    id: 1,
    name: "미라클 모닝 루틴",
    category: "루틴",
    description: "아침에 하루를 깨우는 기본 루틴",
    tasks: ["기상", "물 한 잔", "가벼운 스트레칭", "10분 독서"],
  },
  {
    id: 2,
    name: "퇴근 후 운동 루틴",
    category: "루틴",
    description: "하루를 정리하는 저녁 운동 패턴",
    tasks: ["헬스장 도착", "런닝머신 10분", "하체 위주 웨이트", "마무리 스트레칭"],
  },
  {
    id: 3,
    name: "3박 4일 제주 여행 템플릿",
    category: "여행",
    description: "기본 제주 여행 동선 템플릿",
    tasks: [
      "Day1: 도착 & 렌터카 픽업",
      "Day2: 동부 일정",
      "Day3: 서부 일정",
      "Day4: 기념품 & 귀가",
    ],
  },
];

function buildCalendar(year: number, monthIndex: number) {
  const firstDay = new Date(year, monthIndex, 1);
  const startWeekday = firstDay.getDay(); // 0~6
  const daysInMonth = new Date(year, monthIndex + 1, 0).getDate();
  const cells: (number | null)[] = [];

  for (let i = 0; i < startWeekday; i++) cells.push(null);
  for (let d = 1; d <= daysInMonth; d++) cells.push(d);
  while (cells.length % 7 !== 0) cells.push(null);

  return cells;
}

// =====================================
// 메인 화면
// =====================================

export function StackTodoScreen() {
  const [workspace, setWorkspace] = useState("개인 워크스페이스");
  const [categoryFilter, setCategoryFilter] =
    useState<CategoryFilter>("전체");
  const [tasks, setTasks] = useState<Task[]>(initialTasks);
  const [tab, setTab] = useState<Tab>("today");
  const [selectedDate, setSelectedDate] = useState<string>(TODAY_ISO);
  const [editingTaskId, setEditingTaskId] = useState<number | null>(null);

  const editingTask = useMemo(
    () => tasks.find((t) => t.id === editingTaskId) ?? null,
    [editingTaskId, tasks]
  );

  const filtered = useMemo(
    () =>
      tasks.filter(
        (t) =>
          t.date === selectedDate &&
          (categoryFilter === "전체" ? true : t.category === categoryFilter)
      ),
    [tasks, selectedDate, categoryFilter]
  );

  const sorted = useMemo(
    () =>
      [...filtered].sort((a, b) => {
        if (a.sortOrder !== b.sortOrder) return a.sortOrder - b.sortOrder;
        return a.id - b.id;
      }),
    [filtered]
  );

  const titleText =
    tab === "today" ? "오늘" : tab === "calendar" ? "달력" : "루틴";

  const handleComplete = (id: number) => {
    setTasks((prev) =>
      prev.map((t) =>
        t.id === id ? { ...t, status: "DONE", progressPercent: 100 } : t
      )
    );
  };

  const handleDelete = (id: number) => {
    setTasks((prev) => prev.filter((t) => t.id !== id));
    if (editingTaskId === id) {
      setEditingTaskId(null);
    }
  };

  const handleAddMock = () => {
    setTasks((prev) => {
      const nextId = prev.length ? Math.max(...prev.map((t) => t.id)) + 1 : 1;
      const baseOrder = prev
        .filter((t) => t.date === selectedDate)
        .reduce((max, t) => Math.max(max, t.sortOrder), -1);

      return [
        ...prev,
        {
          id: nextId,
          title: `새 Task #${nextId}`,
          category: "기타",
          hasTime: false,
          status: "TODO",
          progressPercent: 0,
          date: selectedDate,
          sortOrder: baseOrder + 1,
        },
      ];
    });
  };

  const handleApplyRoutineToDate = (groupId: number) => {
    const group = initialRoutineGroups.find((g) => g.id === groupId);
    if (!group) return;

    setTasks((prev) => {
      const baseId = prev.length ? Math.max(...prev.map((t) => t.id)) + 1 : 1;
      const baseOrder = prev
        .filter((t) => t.date === selectedDate)
        .reduce((max, t) => Math.max(max, t.sortOrder), -1);

      const category: CategoryFilter =
        group.category === "여행" ? "여행" : "루틴";

      const newTasks: Task[] = group.tasks.map((title, idx) => ({
        id: baseId + idx,
        title,
        category,
        hasTime: false,
        status: "TODO",
        progressPercent: 0,
        date: selectedDate,
        sortOrder: baseOrder + idx + 1,
      }));

      return [...prev, ...newTasks];
    });

    setTab("today");
  };

  // 상세 모달에서 위/아래 이동
  const moveTask = (taskId: number, direction: "up" | "down") => {
    setTasks((prev) => {
      const target = prev.find((t) => t.id === taskId);
      if (!target) return prev;
      const date = target.date;

      const sameDate = prev
        .filter((t) => t.date === date)
        .sort((a, b) => {
          if (a.sortOrder !== b.sortOrder) return a.sortOrder - b.sortOrder;
          return a.id - b.id;
        });

      const index = sameDate.findIndex((t) => t.id === taskId);
      if (index === -1) return prev;

      const newIndex = direction === "up" ? index - 1 : index + 1;
      if (newIndex < 0 || newIndex >= sameDate.length) return prev;

      const updatedSame = [...sameDate];
      const [moved] = updatedSame.splice(index, 1);
      updatedSame.splice(newIndex, 0, moved);

      // sortOrder 재부여
      const updated = prev.map((t) => {
        if (t.date !== date) return t;
        const foundIndex = updatedSame.findIndex((x) => x.id === t.id);
        if (foundIndex === -1) return t;
        return { ...t, sortOrder: foundIndex };
      });

      return updated;
    });
  };

  return (
    <SafeAreaView style={styles.screen}>
      <View style={styles.phoneFrame}>
        {/* 헤더 */}
        <View style={styles.header}>
          <View style={styles.headerTopRow}>
            <View style={styles.headerTitleBlock}>
              <Text style={styles.headerStackLabel}>Stack</Text>
              <Text style={styles.headerSubLabel}>
                {titleText} · {formatDateLabel(selectedDate)}
              </Text>
            </View>
            <View style={styles.avatarButton}>
              <Text style={styles.avatarEmoji}>☺</Text>
            </View>
          </View>

          <View style={styles.workspaceRow}>
            <TouchableOpacity
              style={styles.workspaceButton}
              onPress={() =>
                setWorkspace((prev) =>
                  prev === "개인 워크스페이스"
                    ? "공유 워크스페이스"
                    : "개인 워크스페이스"
                )
              }
            >
              <Text
                style={styles.workspaceText}
                numberOfLines={1}
                ellipsizeMode="tail"
              >
                {workspace}
              </Text>
              <Text style={styles.workspaceArrow}>▼</Text>
            </TouchableOpacity>
            <Text style={styles.workspaceHint}>탭해서 워크스페이스 전환</Text>
          </View>

          {tab === "today" && (
            <ScrollView
              horizontal
              showsHorizontalScrollIndicator={false}
              style={styles.categoryScroll}
              contentContainerStyle={styles.categoryScrollContent}
            >
              {CATEGORIES.map((cat) => {
                const selected = cat === categoryFilter;
                const style = CATEGORY_STYLES[cat];
                return (
                  <TouchableOpacity
                    key={cat}
                    onPress={() => setCategoryFilter(cat)}
                    style={[
                      styles.categoryChip,
                      selected
                        ? {
                            backgroundColor: style.accent,
                            borderColor: "transparent",
                          }
                        : {
                            backgroundColor: style.pillBg,
                            borderColor: style.pillBorder,
                          },
                    ]}
                  >
                    <Text
                      style={[
                        styles.categoryChipText,
                        selected
                          ? { color: "#FFFFFF" }
                          : { color: style.pillText },
                      ]}
                    >
                      {cat}
                    </Text>
                  </TouchableOpacity>
                );
              })}
            </ScrollView>
          )}
        </View>

        {/* 메인 영역 */}
        <View style={styles.main}>
          {tab === "today" && (
            <ScrollView
              style={styles.todayScroll}
              contentContainerStyle={styles.todayContent}
            >
              {sorted.length === 0 && (
                <View style={styles.emptyBox}>
                  <Text style={styles.emptyMainText}>
                    이 날짜에 할 일이 없어요 ✨
                  </Text>
                  <Text style={styles.emptySubText}>
                    아래 + 버튼이나 루틴 탭에서 Task를 추가해보세요.
                  </Text>
                </View>
              )}

              {sorted.map((task) => (
                <SwipeableTaskRow
                  key={task.id}
                  task={task}
                  onComplete={() => handleComplete(task.id)}
                  onDelete={() => handleDelete(task.id)}
                  onTap={() => setEditingTaskId(task.id)}
                />
              ))}

              <TouchableOpacity
                onPress={handleAddMock}
                style={styles.addTaskButton}
              >
                <Text style={styles.addTaskButtonText}>
                  + 이 날짜 Task 추가
                </Text>
              </TouchableOpacity>
            </ScrollView>
          )}

          {tab === "calendar" && (
            <CalendarView
              tasks={tasks}
              selectedDate={selectedDate}
              onSelectDate={(iso) => {
                setSelectedDate(iso);
                setTab("today");
              }}
            />
          )}

          {tab === "routine" && (
            <RoutineView
              groups={initialRoutineGroups}
              selectedDate={selectedDate}
              onApplyRoutineToDate={handleApplyRoutineToDate}
            />
          )}
        </View>

        {/* 하단 탭 바 */}
        <View style={styles.tabBar}>
          <TabButton
            label="오늘"
            icon="☀"
            active={tab === "today"}
            onPress={() => setTab("today")}
          />
          <TabButton
            label="달력"
            icon="📅"
            active={tab === "calendar"}
            onPress={() => setTab("calendar")}
          />
          <TabButton
            label="루틴"
            icon="🧩"
            active={tab === "routine"}
            onPress={() => setTab("routine")}
          />
        </View>

        {/* 플로팅 + 버튼 */}
        {tab === "today" && (
          <TouchableOpacity
            onPress={handleAddMock}
            style={styles.fab}
            activeOpacity={0.8}
          >
            <Text style={styles.fabText}>+</Text>
          </TouchableOpacity>
        )}

        {/* Task 상세 편집 모달 */}
        {editingTask && (
          <TaskEditSheet
            task={editingTask}
            onClose={() => setEditingTaskId(null)}
            onSave={(updated) => {
              setTasks((prev) =>
                prev.map((t) => (t.id === updated.id ? updated : t))
              );
              setEditingTaskId(null);
            }}
            onDelete={() => handleDelete(editingTask.id)}
            onMoveUp={() => moveTask(editingTask.id, "up")}
            onMoveDown={() => moveTask(editingTask.id, "down")}
          />
        )}
      </View>
    </SafeAreaView>
  );
}

// =====================================
// 탭 버튼
// =====================================

type TabButtonProps = {
  label: string;
  icon: string;
  active: boolean;
  onPress: () => void;
};

function TabButton({ label, icon, active, onPress }: TabButtonProps) {
  return (
    <TouchableOpacity
      onPress={onPress}
      style={styles.tabButton}
      activeOpacity={0.8}
    >
      <Text style={[styles.tabIcon, active && styles.tabActiveText]}>
        {icon}
      </Text>
      <Text style={[styles.tabLabel, active && styles.tabActiveText]}>
        {label}
      </Text>
      {active && <View style={styles.tabIndicator} />}
    </TouchableOpacity>
  );
}

// =====================================
// Task Row + 카드 (스와이프)
// =====================================

// =====================================
// Task Row + 카드 (스와이프)
// =====================================

type SwipeableTaskRowProps = {
  task: Task;
  onComplete: () => void;
  onDelete: () => void;
  onTap: () => void;
};

function SwipeableTaskRow({
  task,
  onComplete,
  onDelete,
  onTap,
}: SwipeableTaskRowProps) {
  const translateX = useRef(new Animated.Value(0)).current;

  const reset = () => {
    Animated.timing(translateX, {
      toValue: 0,
      duration: 150,
      useNativeDriver: false, // web에서도 경고 안 나게
    }).start();
  };

  const panResponder = useRef(
    PanResponder.create({
      // 터치 시작할 때 이 뷰가 제스처를 가져갈지 여부
      onStartShouldSetPanResponder: () => true,
      onMoveShouldSetPanResponder: (_evt, gestureState) =>
        Math.abs(gestureState.dx) > 2, // 살짝 움직이면 수평 제스처로 인식

      onPanResponderGrant: () => {
        // 여기서는 따로 할 건 없음. 필요하면 나중에 flag 추가 가능
      },

      onPanResponderMove: (_evt, gestureState) => {
        const delta = gestureState.dx; // 시작점 대비 수평 이동
        const limited = Math.max(-120, Math.min(120, delta)); // -120 ~ 120 제한
        translateX.setValue(limited);
      },

      onPanResponderRelease: (_evt, gestureState) => {
        const delta = gestureState.dx;
        const absDelta = Math.abs(delta);
        const swipeThreshold = 60; // 이 이상이면 완료/삭제
        const tapThreshold = 5; // 이 이하면 탭으로 간주

        // 거의 안 움직였으면 → 탭
        if (absDelta <= tapThreshold) {
          onTap();
          reset();
          return;
        }

        // 오른쪽 스와이프 → 완료
        if (delta > swipeThreshold) {
          onComplete();
          reset();
          return;
        }

        // 왼쪽 스와이프 → 삭제
        if (delta < -swipeThreshold) {
          onDelete();
          reset();
          return;
        }

        // 애매하면 그냥 원위치
        reset();
      },

      onPanResponderTerminate: () => {
        reset();
      },
    })
  ).current;

  return (
    <View style={styles.taskRow}>
      {/* 배경: 슬라이드 액션 힌트 */}
      <View style={styles.swipeHintRow} pointerEvents="none">
        <View style={styles.swipeLeftBg}>
          <Text style={styles.swipeText}>오른쪽으로 스와이프 → 완료</Text>
        </View>
        <View style={styles.swipeRightBg}>
          <Text style={styles.swipeText}>왼쪽으로 스와이프 → 삭제</Text>
        </View>
      </View>

      {/* 실제 카드 */}
      <Animated.View
        style={[
          styles.taskCardWrapper,
          {
            transform: [{ translateX }],
          },
        ]}
        {...panResponder.panHandlers}
      >
        <TaskCard task={task} />
      </Animated.View>
    </View>
  );
}

type TaskCardProps = { task: Task };

function TaskCard({ task }: TaskCardProps) {
  const statusText = task.status === "DONE" ? "완료" : "진행 중";
  const timeText = task.hasTime && task.time ? ` · ${task.time}` : "";
  const style = CATEGORY_STYLES[task.category];
  const progress = Math.max(0, Math.min(100, task.progressPercent));

  return (
    <View style={styles.taskCard}>
      <View
        style={[
          styles.taskCategoryBar,
          { backgroundColor: style.accent },
        ]}
      />
      <View style={styles.taskCardContent}>
        <View style={styles.taskCardLeft}>
          <View style={styles.taskChipRow}>
            {task.pinned && (
              <View style={styles.pinnedChip}>
                <Text style={styles.pinnedChipText}>PINNED</Text>
              </View>
            )}
            <View
              style={[
                styles.categoryChipSmall,
                {
                  backgroundColor: style.pillBg,
                  borderColor: style.pillBorder,
                },
              ]}
            >
              <Text
                style={[
                  styles.categoryChipSmallText,
                  { color: style.pillText },
                ]}
              >
                {task.category}
              </Text>
            </View>
          </View>
          <Text style={styles.taskTitle} numberOfLines={1}>
            {task.title}
          </Text>
          <Text style={styles.taskMeta}>
            {statusText}
            {timeText}
          </Text>
        </View>

        <View style={styles.taskCardRight}>
          <Text style={styles.progressText}>{progress}%</Text>
          <View style={styles.progressBar}>
            <View
              style={[
                styles.progressFill,
                { width: `${progress}%`, backgroundColor: style.accent },
              ]}
            />
          </View>
        </View>
      </View>
    </View>
  );
}

// =====================================
// 달력 뷰
// =====================================

type CalendarViewProps = {
  tasks: Task[];
  selectedDate: string;
  onSelectDate: (iso: string) => void;
};

function CalendarView({
  tasks,
  selectedDate,
  onSelectDate,
}: CalendarViewProps) {
  const now = new Date();
  const [year, setYear] = useState(now.getFullYear());
  const [monthIndex, setMonthIndex] = useState(now.getMonth());

  const cells = useMemo(
    () => buildCalendar(year, monthIndex),
    [year, monthIndex]
  );

  const today = now.getDate();
  const thisMonth = now.getMonth();
  const thisYear = now.getFullYear();

  const monthLabel = `${year}.${String(monthIndex + 1).padStart(2, "0")}`;
  const weekdays = ["일", "월", "화", "수", "목", "금", "토"];

  const goPrev = () => {
    setMonthIndex((prev) => {
      if (prev === 0) {
        setYear((y) => y - 1);
        return 11;
      }
      return prev - 1;
    });
  };

  const goNext = () => {
    setMonthIndex((prev) => {
      if (prev === 11) {
        setYear((y) => y + 1);
        return 0;
      }
      return prev + 1;
    });
  };

  return (
    <View style={styles.calendarContainer}>
      <View style={styles.calendarHeaderRow}>
        <TouchableOpacity style={styles.calendarNavButton} onPress={goPrev}>
          <Text style={styles.calendarNavText}>◀</Text>
        </TouchableOpacity>
        <Text style={styles.calendarTitle}>{monthLabel}</Text>
        <TouchableOpacity style={styles.calendarNavButton} onPress={goNext}>
          <Text style={styles.calendarNavText}>▶</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.calendarWeekRow}>
        {weekdays.map((w) => (
          <Text key={w} style={styles.calendarWeekday}>
            {w}
          </Text>
        ))}
      </View>

      <View style={styles.calendarGrid}>
        {cells.map((d, idx) => {
          if (d == null) {
            return <View key={idx} style={styles.calendarEmptyCell} />;
          }

          const iso = toISODate(year, monthIndex + 1, d);
          const isToday =
            d === today && monthIndex === thisMonth && year === thisYear;
          const isSelected = iso === selectedDate;
          const hasTasks = tasks.some((t) => t.date === iso);

          let cellStyle = styles.calendarDayCell;
          let textStyle = styles.calendarDayText;
          if (isSelected) {
            cellStyle = [cellStyle, styles.calendarDaySelected] as any;
            textStyle = [textStyle, styles.calendarDaySelectedText] as any;
          } else if (isToday) {
            cellStyle = [cellStyle, styles.calendarDayToday] as any;
          }

          return (
            <TouchableOpacity
              key={idx}
              style={cellStyle}
              onPress={() => onSelectDate(iso)}
            >
              <Text style={textStyle}>{d}</Text>
              {hasTasks && <View style={styles.calendarDot} />}
            </TouchableOpacity>
          );
        })}
      </View>

      <Text style={styles.calendarHelpText}>
        ● 초록 점이 있는 날짜는 Task가 있는 날입니다. 날짜를 탭하면 해당 날짜
        Stack 화면으로 이동합니다.
      </Text>
    </View>
  );
}

// =====================================
// 루틴 뷰
// =====================================

type RoutineViewProps = {
  groups: RoutineGroup[];
  selectedDate: string;
  onApplyRoutineToDate: (groupId: number) => void;
};

function RoutineView({
  groups,
  selectedDate,
  onApplyRoutineToDate,
}: RoutineViewProps) {
  const dateLabel = formatDateLabel(selectedDate);

  return (
    <ScrollView
      style={styles.routineScroll}
      contentContainerStyle={styles.routineContent}
    >
      {groups.map((g) => {
        const style =
          g.category === "여행"
            ? CATEGORY_STYLES["여행"]
            : CATEGORY_STYLES["루틴"];
        return (
          <View key={g.id} style={styles.routineCard}>
            <View style={styles.routineHeaderRow}>
              <View style={styles.routineHeaderLeft}>
                <View style={styles.routineCategoryRow}>
                  <View
                    style={[
                      styles.routineCategoryChip,
                      {
                        backgroundColor: style.pillBg,
                        borderColor: style.pillBorder,
                      },
                    ]}
                  >
                    <Text
                      style={[
                        styles.routineCategoryText,
                        { color: style.pillText },
                      ]}
                    >
                      {g.category}
                    </Text>
                  </View>
                  <Text style={styles.routineTaskCount}>
                    {g.tasks.length}개의 Task
                  </Text>
                </View>
                <Text style={styles.routineTitle} numberOfLines={1}>
                  {g.name}
                </Text>
                {g.description && (
                  <Text style={styles.routineDescription} numberOfLines={2}>
                    {g.description}
                  </Text>
                )}
              </View>
            </View>

            <View style={styles.routineTaskList}>
              {g.tasks.map((t, idx) => (
                <View key={idx} style={styles.routineTaskRow}>
                  <View style={styles.routineBullet} />
                  <Text style={styles.routineTaskText} numberOfLines={1}>
                    {t}
                  </Text>
                </View>
              ))}
            </View>

            <View style={styles.routineFooterRow}>
              <TouchableOpacity style={styles.routineDetailButton}>
                <Text style={styles.routineDetailText}>상세 보기</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={[
                  styles.routineApplyButton,
                  { backgroundColor: style.accent },
                ]}
                onPress={() => onApplyRoutineToDate(g.id)}
              >
                <Text style={styles.routineApplyText}>
                  {dateLabel} Stack에 추가
                </Text>
              </TouchableOpacity>
            </View>
          </View>
        );
      })}

      {groups.length === 0 && (
        <View style={styles.emptyBox}>
          <Text style={styles.emptyMainText}>아직 저장된 루틴이 없어요.</Text>
          <Text style={styles.emptySubText}>
            오늘 작성한 Task 묶음을 루틴으로 저장해보세요.
          </Text>
        </View>
      )}
    </ScrollView>
  );
}

// =====================================
// Task 상세 모달
// =====================================

type TaskEditSheetProps = {
  task: Task;
  onClose: () => void;
  onSave: (task: Task) => void;
  onDelete: () => void;
  onMoveUp: () => void;
  onMoveDown: () => void;
};

function TaskEditSheet({
  task,
  onClose,
  onSave,
  onDelete,
  onMoveUp,
  onMoveDown,
}: TaskEditSheetProps) {
  const [title, setTitle] = useState(task.title);
  const [category, setCategory] = useState<CategoryFilter>(task.category);
  const [hasTime, setHasTime] = useState(task.hasTime);
  const [time, setTime] = useState(task.time ?? "");
  const [progress, setProgress] = useState(task.progressPercent);
  const [pinned, setPinned] = useState(!!task.pinned);
  const [status, setStatus] = useState<TaskStatus>(task.status);

  const style = CATEGORY_STYLES[category];

  const handleSave = () => {
    onSave({
      ...task,
      title,
      category,
      hasTime,
      time: hasTime && time ? time : undefined,
      progressPercent: progress,
      pinned,
      status,
    });
  };

  return (
    <Modal
      visible
      animationType="fade"
      transparent
      onRequestClose={onClose}
    >
      <View style={styles.modalOverlay}>
        <View style={styles.modalCard}>
          <View style={styles.modalHandle} />
          <View style={styles.modalHeaderRow}>
            <Text style={styles.modalTitle}>Task 상세</Text>
            <TouchableOpacity onPress={onClose}>
              <Text style={styles.modalCloseText}>닫기</Text>
            </TouchableOpacity>
          </View>
          <Text style={styles.modalMeta}>
            {formatDateLabel(task.date)} · ID {task.id}
          </Text>

          <ScrollView
            style={styles.modalScroll}
            contentContainerStyle={styles.modalContent}
          >
            <View style={styles.modalField}>
              <Text style={styles.modalLabel}>제목</Text>
              <TextInput
                style={styles.modalTextInput}
                value={title}
                onChangeText={setTitle}
                placeholder="Task 제목을 입력하세요"
              />
            </View>

            <View style={styles.modalField}>
              <Text style={styles.modalLabel}>카테고리</Text>
              <View style={styles.modalCategoryRow}>
                {TASK_CATEGORIES.map((cat) => {
                  const s = CATEGORY_STYLES[cat];
                  const selected = cat === category;
                  return (
                    <TouchableOpacity
                      key={cat}
                      onPress={() => setCategory(cat)}
                      style={[
                        styles.modalCategoryChip,
                        selected
                          ? {
                              backgroundColor: s.accent,
                              borderColor: "transparent",
                            }
                          : {
                              backgroundColor: s.pillBg,
                              borderColor: s.pillBorder,
                            },
                      ]}
                    >
                      <Text
                        style={[
                          styles.modalCategoryChipText,
                          selected
                            ? { color: "#FFFFFF" }
                            : { color: s.pillText },
                        ]}
                      >
                        {cat}
                      </Text>
                    </TouchableOpacity>
                  );
                })}
              </View>
            </View>

            <View style={styles.modalFieldRow}>
              <View style={styles.modalSwitchRow}>
                <Switch
                  value={hasTime}
                  onValueChange={setHasTime}
                  thumbColor={hasTime ? style.accent : "#E5E7EB"}
                  trackColor={{ true: style.accent, false: "#CBD5E1" }}
                />
                <Text style={styles.modalSwitchLabel}>시간 사용</Text>
              </View>
              {hasTime && (
                <TextInput
                  style={styles.modalTimeInput}
                  value={time}
                  onChangeText={setTime}
                  placeholder="HH:MM"
                />
              )}
            </View>

            <View style={styles.modalField}>
              <Text style={styles.modalLabel}>진행률 ({progress}%)</Text>
              {/* Slider: 플랫폼에 따라 expo / community slider를 쓸 수도 있음 */}
              <Slider
                minimumValue={0}
                maximumValue={100}
                step={1}
                value={progress}
                onValueChange={(val) => setProgress(Math.round(val))}
              />
            </View>

            <View style={styles.modalFieldRow}>
              <View style={styles.modalSwitchRow}>
                <Switch
                  value={pinned}
                  onValueChange={setPinned}
                  thumbColor={pinned ? style.accent : "#E5E7EB"}
                  trackColor={{ true: style.accent, false: "#CBD5E1" }}
                />
                <Text style={styles.modalSwitchLabel}>상단 고정</Text>
              </View>

              <View style={styles.modalStatusRow}>
                <TouchableOpacity
                  onPress={() => setStatus("TODO")}
                  style={[
                    styles.modalStatusChip,
                    status === "TODO" && {
                      backgroundColor: style.accent,
                    },
                  ]}
                >
                  <Text
                    style={[
                      styles.modalStatusText,
                      status === "TODO" && { color: "#FFFFFF" },
                    ]}
                  >
                    진행 중
                  </Text>
                </TouchableOpacity>
                <TouchableOpacity
                  onPress={() => setStatus("DONE")}
                  style={[
                    styles.modalStatusChip,
                    status === "DONE" && {
                      backgroundColor: style.accent,
                    },
                  ]}
                >
                  <Text
                    style={[
                      styles.modalStatusText,
                      status === "DONE" && { color: "#FFFFFF" },
                    ]}
                  >
                    완료
                  </Text>
                </TouchableOpacity>
              </View>
            </View>

            {/* 위/아래 정렬 버튼 */}
            <View style={styles.modalSortRow}>
              <TouchableOpacity
                style={styles.modalSortButton}
                onPress={onMoveUp}
              >
                <Text style={styles.modalSortButtonText}>위로</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={styles.modalSortButton}
                onPress={onMoveDown}
              >
                <Text style={styles.modalSortButtonText}>아래로</Text>
              </TouchableOpacity>
            </View>

            <View style={styles.modalDangerRow}>
              <TouchableOpacity
                style={styles.modalDeleteButton}
                onPress={onDelete}
              >
                <Text style={styles.modalDeleteText}>Task 삭제</Text>
              </TouchableOpacity>
            </View>
          </ScrollView>

          <View style={styles.modalFooterRow}>
            <TouchableOpacity
              onPress={onClose}
              style={styles.modalCancelButton}
            >
              <Text style={styles.modalCancelText}>취소</Text>
            </TouchableOpacity>
            <TouchableOpacity
              onPress={handleSave}
              style={[styles.modalSaveButton, { backgroundColor: style.accent }]}
            >
              <Text style={styles.modalSaveText}>저장</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </Modal>
  );
}

// =====================================
// 스타일
// =====================================

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    backgroundColor: "#020617", // slate-900
    alignItems: "center",
    justifyContent: "center",
    paddingVertical: 16,
  },
  phoneFrame: {
    width: 390,
    height: 800,
    backgroundColor: "#F1F5F9", // slate-100
    borderRadius: 32,
    overflow: "hidden",
    shadowColor: "#000",
    shadowOpacity: 0.2,
    shadowRadius: 16,
    shadowOffset: { width: 0, height: 6 },
    elevation: 10,
  },
  header: {
    backgroundColor: "#FFFFFF",
    borderBottomColor: "#E2E8F0",
    borderBottomWidth: StyleSheet.hairlineWidth,
    paddingHorizontal: 16,
    paddingTop: 16,
    paddingBottom: 8,
  },
  headerTopRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },
  headerTitleBlock: {
    flexDirection: "column",
  },
  headerStackLabel: {
    fontSize: 11,
    color: "#94A3B8",
  },
  headerSubLabel: {
    marginTop: 2,
    fontSize: 12,
    color: "#64748B",
  },
  avatarButton: {
    width: 32,
    height: 32,
    borderRadius: 16,
    backgroundColor: "#E2E8F0",
    alignItems: "center",
    justifyContent: "center",
  },
  avatarEmoji: {
    fontSize: 14,
    color: "#64748B",
  },
  workspaceRow: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    marginTop: 8,
  },
  workspaceButton: {
    flexDirection: "row",
    alignItems: "center",
    borderWidth: 1,
    borderColor: "#E2E8F0",
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 999,
    backgroundColor: "#F8FAFC",
    maxWidth: 220,
  },
  workspaceText: {
    fontSize: 11,
    color: "#0F172A",
    flexShrink: 1,
  },
  workspaceArrow: {
    fontSize: 9,
    color: "#94A3B8",
    marginLeft: 4,
  },
  workspaceHint: {
    fontSize: 9,
    color: "#94A3B8",
  },
  categoryScroll: {
    marginTop: 8,
  },
  categoryScrollContent: {
    paddingBottom: 4,
    paddingRight: 4,
  },
  categoryChip: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 999,
    borderWidth: 1,
    marginRight: 6,
  },
  categoryChipText: {
    fontSize: 11,
  },
  main: {
    flex: 1,
    backgroundColor: "#F8FAFC",
  },
  todayScroll: {
    flex: 1,
  },
  todayContent: {
    paddingHorizontal: 16,
    paddingTop: 8,
    paddingBottom: 80,
  },
  emptyBox: {
    alignItems: "center",
    justifyContent: "center",
    paddingVertical: 40,
  },
  emptyMainText: {
    fontSize: 12,
    color: "#94A3B8",
  },
  emptySubText: {
    marginTop: 4,
    fontSize: 11,
    color: "#A1A1AA",
    textAlign: "center",
  },
  addTaskButton: {
    marginTop: 8,
    paddingVertical: 8,
    borderRadius: 16,
    borderWidth: 1,
    borderStyle: "dashed",
    borderColor: "#CBD5E1",
    backgroundColor: "#E2E8F0",
    alignItems: "center",
  },
  addTaskButtonText: {
    fontSize: 12,
    color: "#64748B",
  },
  tabBar: {
    position: "absolute",
    left: 0,
    right: 0,
    bottom: 0,
    height: 56,
    backgroundColor: "#FFFFFF",
    borderTopColor: "#E2E8F0",
    borderTopWidth: StyleSheet.hairlineWidth,
    flexDirection: "row",
  },
  tabButton: {
    flex: 1,
    height: "100%",
    alignItems: "center",
    justifyContent: "center",
  },
  tabIcon: {
    fontSize: 15,
    marginBottom: 2,
    color: "#9CA3AF",
  },
  tabLabel: {
    fontSize: 11,
    color: "#9CA3AF",
  },
  tabActiveText: {
    color: "#020617",
  },
  tabIndicator: {
    marginTop: 2,
    width: 32,
    height: 2,
    borderRadius: 999,
    backgroundColor: "#020617",
  },
  fab: {
    position: "absolute",
    bottom: 72,
    right: 24,
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: "#020617",
    alignItems: "center",
    justifyContent: "center",
    shadowColor: "#000",
    shadowOpacity: 0.25,
    shadowRadius: 10,
    shadowOffset: { width: 0, height: 4 },
    elevation: 8,
  },
  fabText: {
    fontSize: 28,
    color: "#FFFFFF",
    marginTop: -2,
  },

  // 스와이프 / Task Row
  taskRow: {
    height: 80,
    marginBottom: 8,
  },
  swipeLeftBg: {
    flex: 1,
    backgroundColor: "rgba(34,197,94,0.9)",
    justifyContent: "center",
    paddingHorizontal: 16,
  },
  swipeRightBg: {
    flex: 1,
    backgroundColor: "rgba(239,68,68,0.9)",
    justifyContent: "center",
    alignItems: "flex-end",
    paddingHorizontal: 16,
  },
  swipeText: {
    fontSize: 10,
    color: "#FFFFFF",
    fontWeight: "600",
  },
  taskCardWrapper: {
    flex: 1,
  },
  taskCard: {
    flex: 1,
    backgroundColor: "#FFFFFF",
    borderRadius: 24,
    borderWidth: 1,
    borderColor: "#E5E7EB",
    paddingHorizontal: 16,
    paddingVertical: 10,
    shadowColor: "#000",
    shadowOpacity: 0.08,
    shadowRadius: 8,
    shadowOffset: { width: 0, height: 3 },
    elevation: 3,
  },
  taskCategoryBar: {
    position: "absolute",
    left: 0,
    top: 0,
    bottom: 0,
    width: 4,
    borderTopLeftRadius: 24,
    borderBottomLeftRadius: 24,
  },
  taskCardContent: {
    flex: 1,
    flexDirection: "row",
  },
  taskCardLeft: {
    flex: 1,
    paddingRight: 8,
  },
  taskChipRow: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 2,
  },
  pinnedChip: {
    paddingHorizontal: 6,
    paddingVertical: 2,
    borderRadius: 999,
    backgroundColor: "#FEF3C7",
    borderWidth: 1,
    borderColor: "#FDE68A",
    marginRight: 4,
  },
  pinnedChipText: {
    fontSize: 9,
    color: "#92400E",
  },
  categoryChipSmall: {
    paddingHorizontal: 8,
    paddingVertical: 2,
    borderRadius: 999,
    borderWidth: 1,
  },
  categoryChipSmallText: {
    fontSize: 10,
  },
  taskTitle: {
    marginTop: 2,
    fontSize: 13,
    fontWeight: "600",
    color: "#0F172A",
  },
  taskMeta: {
    marginTop: 4,
    fontSize: 11,
    color: "#6B7280",
  },
  taskCardRight: {
    alignItems: "flex-end",
    justifyContent: "space-between",
  },
  progressText: {
    fontSize: 10,
    color: "#9CA3AF",
  },
  progressBar: {
    marginTop: 4,
    width: 56,
    height: 6,
    borderRadius: 999,
    backgroundColor: "#E5E7EB",
    overflow: "hidden",
  },
  progressFill: {
    height: "100%",
    borderRadius: 999,
  },

  // 달력
  calendarContainer: {
    flex: 1,
    paddingHorizontal: 16,
    paddingTop: 8,
    paddingBottom: 72,
  },
  calendarHeaderRow: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    marginBottom: 12,
  },
  calendarNavButton: {
    width: 32,
    height: 32,
    borderRadius: 16,
    backgroundColor: "#E5E7EB",
    alignItems: "center",
    justifyContent: "center",
  },
  calendarNavText: {
    fontSize: 14,
    color: "#0F172A",
  },
  calendarTitle: {
    fontSize: 14,
    fontWeight: "600",
    color: "#0F172A",
  },
  calendarWeekRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 4,
  },
  calendarWeekday: {
    flex: 1,
    textAlign: "center",
    fontSize: 10,
    color: "#6B7280",
  },
  calendarGrid: {
    flexDirection: "row",
    flexWrap: "wrap",
  },
  calendarEmptyCell: {
    width: "14.2857%",
    height: 40,
  },
  calendarDayCell: {
    width: "14.2857%",
    height: 40,
    borderRadius: 16,
    marginVertical: 2,
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "#FFFFFF",
  },
  calendarDayToday: {
    backgroundColor: "#E5E7EB",
  },
  calendarDaySelected: {
    backgroundColor: "#020617",
  },
  calendarDayText: {
    fontSize: 11,
    color: "#0F172A",
  },
  calendarDaySelectedText: {
    color: "#FFFFFF",
  },
  calendarDot: {
    marginTop: 2,
    width: 6,
    height: 6,
    borderRadius: 3,
    backgroundColor: "#10B981",
  },
  calendarHelpText: {
    marginTop: 12,
    fontSize: 10,
    color: "#9CA3AF",
  },

  // 루틴
  routineScroll: {
    flex: 1,
  },
  routineContent: {
    paddingHorizontal: 16,
    paddingTop: 8,
    paddingBottom: 72,
  },
  routineCard: {
    backgroundColor: "#FFFFFF",
    borderRadius: 24,
    paddingHorizontal: 16,
    paddingVertical: 12,
    borderWidth: 1,
    borderColor: "#E5E7EB",
    marginBottom: 8,
    shadowColor: "#000",
    shadowOpacity: 0.06,
    shadowRadius: 6,
    shadowOffset: { width: 0, height: 2 },
    elevation: 2,
  },
  routineHeaderRow: {
    flexDirection: "row",
    justifyContent: "space-between",
  },
  routineHeaderLeft: {
    flex: 1,
  },
  routineCategoryRow: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 4,
  },
  routineCategoryChip: {
    paddingHorizontal: 8,
    paddingVertical: 2,
    borderRadius: 999,
    borderWidth: 1,
    marginRight: 6,
  },
  routineCategoryText: {
    fontSize: 10,
  },
  routineTaskCount: {
    fontSize: 9,
    color: "#9CA3AF",
  },
  routineTitle: {
    fontSize: 13,
    fontWeight: "600",
    color: "#0F172A",
  },
  routineDescription: {
    marginTop: 4,
    fontSize: 10,
    color: "#6B7280",
  },
  routineTaskList: {
    marginTop: 8,
  },
  routineTaskRow: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 2,
  },
  routineBullet: {
    width: 4,
    height: 4,
    borderRadius: 2,
    backgroundColor: "#9CA3AF",
    marginRight: 6,
  },
  routineTaskText: {
    fontSize: 10,
    color: "#475569",
    flexShrink: 1,
  },
  routineFooterRow: {
    marginTop: 8,
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },
  routineDetailButton: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 999,
    borderWidth: 1,
    borderColor: "#E5E7EB",
    backgroundColor: "#F8FAFC",
  },
  routineDetailText: {
    fontSize: 10,
    color: "#64748B",
  },
  routineApplyButton: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 999,
  },
  routineApplyText: {
    fontSize: 10,
    color: "#FFFFFF",
  },

  // 모달
  modalOverlay: {
    flex: 1,
    backgroundColor: "rgba(0,0,0,0.3)",
    alignItems: "center",
    justifyContent: "center",
    paddingHorizontal: 16,
  },
  modalCard: {
    width: "100%",
    maxWidth: 340,
    maxHeight: "80%",
    backgroundColor: "#FFFFFF",
    borderRadius: 24,
    paddingHorizontal: 16,
    paddingTop: 12,
    paddingBottom: 12,
  },
  modalHandle: {
    alignSelf: "center",
    width: 40,
    height: 4,
    borderRadius: 999,
    backgroundColor: "#CBD5E1",
    marginBottom: 8,
  },
  modalHeaderRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 4,
  },
  modalTitle: {
    fontSize: 14,
    fontWeight: "600",
    color: "#111827",
  },
  modalCloseText: {
    fontSize: 11,
    color: "#9CA3AF",
  },
  modalMeta: {
    fontSize: 10,
    color: "#9CA3AF",
    marginBottom: 6,
  },
  modalScroll: {
    flex: 1,
    marginBottom: 8,
  },
  modalContent: {
    paddingBottom: 8,
  },
  modalField: {
    marginBottom: 10,
  },
  modalLabel: {
    fontSize: 10,
    color: "#6B7280",
    marginBottom: 4,
  },
  modalTextInput: {
    borderWidth: 1,
    borderColor: "#E5E7EB",
    borderRadius: 16,
    paddingHorizontal: 12,
    paddingVertical: Platform.OS === "ios" ? 8 : 4,
    fontSize: 11,
    backgroundColor: "#F8FAFC",
  },
  modalCategoryRow: {
    flexDirection: "row",
    flexWrap: "wrap",
  },
  modalCategoryChip: {
    paddingHorizontal: 10,
    paddingVertical: 6,
    borderRadius: 999,
    borderWidth: 1,
    marginRight: 6,
    marginBottom: 6,
  },
  modalCategoryChipText: {
    fontSize: 10,
  },
  modalFieldRow: {
    marginBottom: 12,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
  },
  modalSwitchRow: {
    flexDirection: "row",
    alignItems: "center",
  },
  modalSwitchLabel: {
    marginLeft: 6,
    fontSize: 10,
    color: "#4B5563",
  },
  modalTimeInput: {
    borderWidth: 1,
    borderColor: "#E5E7EB",
    borderRadius: 12,
    paddingHorizontal: 8,
    paddingVertical: Platform.OS === "ios" ? 6 : 2,
    fontSize: 11,
    backgroundColor: "#F8FAFC",
    minWidth: 70,
    textAlign: "center",
  },
  modalStatusRow: {
    flexDirection: "row",
    backgroundColor: "#F1F5F9",
    borderRadius: 999,
    padding: 2,
  },
  modalStatusChip: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 999,
  },
  modalStatusText: {
    fontSize: 10,
    color: "#6B7280",
  },
  modalSortRow: {
    marginTop: 4,
    flexDirection: "row",
    justifyContent: "space-between",
  },
  modalSortButton: {
    flex: 1,
    marginHorizontal: 2,
    paddingVertical: 6,
    borderRadius: 999,
    borderWidth: 1,
    borderColor: "#E5E7EB",
    backgroundColor: "#F8FAFC",
    alignItems: "center",
  },
  modalSortButtonText: {
    fontSize: 10,
    color: "#4B5563",
  },
  modalDangerRow: {
    marginTop: 8,
    alignItems: "flex-end",
  },
  modalDeleteButton: {
    paddingHorizontal: 10,
    paddingVertical: 6,
    borderRadius: 999,
    borderWidth: 1,
    borderColor: "#FECACA",
    backgroundColor: "#FEF2F2",
  },
  modalDeleteText: {
    fontSize: 10,
    color: "#B91C1C",
  },
  modalFooterRow: {
    flexDirection: "row",
    justifyContent: "flex-end",
    alignItems: "center",
    marginTop: 4,
  },
  modalCancelButton: {
    paddingHorizontal: 12,
    paddingVertical: 8,
    borderRadius: 999,
    borderWidth: 1,
    borderColor: "#E5E7EB",
    backgroundColor: "#F8FAFC",
    marginRight: 6,
  },
  modalCancelText: {
    fontSize: 11,
    color: "#4B5563",
  },
  modalSaveButton: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 999,
  },
  modalSaveText: {
    fontSize: 11,
    color: "#FFFFFF",
  },
    // 스와이프 / Task Row
  swipeHintRow: {
    ...StyleSheet.absoluteFillObject,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    paddingHorizontal: 16,
  },
});