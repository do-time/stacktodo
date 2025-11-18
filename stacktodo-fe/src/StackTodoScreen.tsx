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

/* ------------------ 타입 & 상수 ------------------ */

const CATEGORIES = ["전체", "여행", "루틴", "업무", "기타"] as const;
const TASK_CATEGORIES = ["여행", "루틴", "업무", "기타"] as const;

type CategoryFilter = (typeof CATEGORIES)[number];

const TABS = ["today", "calendar", "routine"] as const;
type Tab = (typeof TABS)[number];

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
  { pillBg: string; pillText: string; pillBorder: string; accent: string }
> = {
  전체: {
    pillBg: "#E5E7EB",
    pillText: "#4B5563",
    pillBorder: "#E5E7EB",
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
    pillBg: "#FFFBEB",
    pillText: "#92400E",
    pillBorder: "#FDE68A",
    accent: "#F59E0B",
  },
  기타: {
    pillBg: "#E5E7EB",
    pillText: "#374151",
    pillBorder: "#E5E7EB",
    accent: "#64748B",
  },
};

/* ------------------ 날짜 유틸 ------------------ */

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

/* ------------------ 캘린더 ------------------ */

function buildCalendar(year: number, monthIndex: number) {
  const firstDay = new Date(year, monthIndex, 1);
  const startWeekday = firstDay.getDay(); // 0(일)~6(토)
  const daysInMonth = new Date(year, monthIndex + 1, 0).getDate();
  const cells: (number | null)[] = [];

  for (let i = 0; i < startWeekday; i++) cells.push(null);
  for (let d = 1; d <= daysInMonth; d++) cells.push(d);
  while (cells.length % 7 !== 0) cells.push(null);

  return cells;
}

/* =========================================================
 *   메인 화면
 * =======================================================*/

export const StackTodoScreen: React.FC = () => {
  const [workspace, setWorkspace] = useState("개인 워크스페이스");
  const [categoryFilter, setCategoryFilter] =
    useState<CategoryFilter>("전체");
  const [tasks, setTasks] = useState<Task[]>(initialTasks);
  const [tab, setTab] = useState<Tab>("today");
  const [selectedDate, setSelectedDate] = useState(TODAY_ISO);
  const [editingTaskId, setEditingTaskId] = useState<number | null>(null);

  const filtered = useMemo(
    () =>
      tasks.filter(
        (t) =>
          t.date === selectedDate &&
          (categoryFilter === "전체" ? true : t.category === categoryFilter)
      ),
    [tasks, categoryFilter, selectedDate]
  );

  const sorted = useMemo(
    () =>
      [...filtered].sort((a, b) => {
        const ao = a.sortOrder ?? 0;
        const bo = b.sortOrder ?? 0;
        if (ao !== bo) return ao - bo;
        return a.id - b.id;
      }),
    [filtered]
  );

  const handleComplete = (id: number) => {
    setTasks((prev) =>
      prev.map((t) =>
        t.id === id ? { ...t, status: "DONE", progressPercent: 100 } : t
      )
    );
  };

  const handleDelete = (id: number) => {
    setTasks((prev) => prev.filter((t) => t.id !== id));
  };

  const handleAddMock = () => {
    setTasks((prev) => {
      const nextId = prev.length ? Math.max(...prev.map((t) => t.id)) + 1 : 1;
      const maxOrder = prev
        .filter((t) => t.date === selectedDate)
        .reduce((m, t) => Math.max(m, t.sortOrder ?? 0), -1);
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
          sortOrder: maxOrder + 1,
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
        .reduce((m, t) => Math.max(m, t.sortOrder ?? 0), -1);
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

  const editingTask =
    editingTaskId != null
      ? tasks.find((t) => t.id === editingTaskId) ?? null
      : null;

  const titleText =
    tab === "today" ? "오늘" : tab === "calendar" ? "달력" : "루틴";

  return (
    <View style={styles.root}>
      <SafeAreaView style={styles.root}>
        {/* 휴대폰 프레임 */}
        <View style={styles.phoneFrame}>
          {/* 헤더: 앱 이름 + 날짜 + 이모티콘 + 워크스페이스 */}
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
                activeOpacity={0.9}
                style={styles.workspaceButton}
                onPress={() =>
                  setWorkspace((prev) =>
                    prev === "개인 워크스페이스"
                      ? "공유 워크스페이스"
                      : "개인 워크스페이스"
                  )
                }
              >
                <Text style={styles.workspaceText} numberOfLines={1}>
                  {workspace}
                </Text>
                <Text style={styles.workspaceArrow}>▼</Text>
              </TouchableOpacity>
              <Text style={styles.workspaceHint}>
                탭해서 워크스페이스 전환
              </Text>
            </View>

            {/* 카테고리 필터 (오늘 탭에서만) */}
            {tab === "today" && (
              <ScrollView
                horizontal
                showsHorizontalScrollIndicator={false}
                contentContainerStyle={styles.categoryScroll}
              >
                {CATEGORIES.map((cat) => {
                  const selected = cat === categoryFilter;
                  const style = CATEGORY_STYLES[cat];
                  return (
                    <TouchableOpacity
                      key={cat}
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
                      onPress={() => setCategoryFilter(cat)}
                      activeOpacity={0.85}
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
                style={{ flex: 1 }}
                contentContainerStyle={styles.todayContent}
              >
                {sorted.length === 0 && (
                  <View style={styles.emptyBox}>
                    <Text style={styles.emptyTitle}>
                      이 날짜에 할 일이 없어요 ✨
                    </Text>
                    <Text style={styles.emptyDesc}>
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
                  activeOpacity={0.9}
                  style={styles.addDateButton}
                >
                  <Text style={styles.addDateButtonText}>
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

          {/* 하단 탭 바 (오늘 / 달력 / 루틴) */}
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

          {/* 오늘 탭용 + FAB */}
          {tab === "today" && (
            <TouchableOpacity
              onPress={handleAddMock}
              activeOpacity={0.9}
              style={styles.fab}
            >
              <Text style={styles.fabText}>+</Text>
            </TouchableOpacity>
          )}

          {/* Task 편집 모달 */}
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
            />
          )}
        </View>
      </SafeAreaView>
    </View>
  );
};

/* =========================================================
 *   하단 탭 버튼
 * =======================================================*/

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
      activeOpacity={0.9}
      style={styles.tabButton}
    >
      <Text
        style={[styles.tabIcon, active ? styles.tabActiveText : undefined]}
      >
        {icon}
      </Text>
      <Text
        style={[styles.tabLabel, active ? styles.tabActiveText : undefined]}
      >
        {label}
      </Text>
      {active && <View style={styles.tabIndicator} />}
    </TouchableOpacity>
  );
}

/* =========================================================
 *   스와이프 가능한 Task Row (PanResponder)
 * =======================================================*/

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
      useNativeDriver: false,
    }).start();
  };

  const panResponder = useRef(
    PanResponder.create({
      onStartShouldSetPanResponder: () => true,
      onMoveShouldSetPanResponder: (_evt, gestureState) =>
        Math.abs(gestureState.dx) > Math.abs(gestureState.dy) &&
        Math.abs(gestureState.dx) > 2,
      onPanResponderMove: (_evt, gestureState) => {
        const delta = gestureState.dx;
        const limited = Math.max(-120, Math.min(120, delta));
        translateX.setValue(limited);
      },
      onPanResponderRelease: (_evt, gestureState) => {
        const delta = gestureState.dx;
        const absDelta = Math.abs(delta);
        const swipeThreshold = 60;
        const tapThreshold = 5;

        if (absDelta <= tapThreshold) {
          onTap();
          reset();
          return;
        }

        if (delta > swipeThreshold) {
          onComplete();
          reset();
          return;
        }

        if (delta < -swipeThreshold) {
          onDelete();
          reset();
          return;
        }

        reset();
      },
      onPanResponderTerminate: () => {
        reset();
      },
    })
  ).current;

  return (
    <View style={styles.taskRow}>
      <View style={styles.swipeHintRow} pointerEvents="none">
        <View style={styles.swipeLeftBg}>
          <Text style={styles.swipeText}>오른쪽으로 스와이프 → 완료</Text>
        </View>
        <View style={styles.swipeRightBg}>
          <Text style={styles.swipeText}>왼쪽으로 스와이프 → 삭제</Text>
        </View>
      </View>

      <Animated.View
        style={[styles.taskCardWrapper, { transform: [{ translateX }] }]}
        {...panResponder.panHandlers}
      >
        <TaskCard task={task} />
      </Animated.View>
    </View>
  );
}

/* =========================================================
 *   Task 카드
 * =======================================================*/

type TaskCardProps = {
  task: Task;
};

function TaskCard({ task }: TaskCardProps) {
  const statusText = task.status === "DONE" ? "완료" : "진행 중";
  const timeText = task.hasTime && task.time ? ` · ${task.time}` : "";
  const progress = Math.max(0, Math.min(100, task.progressPercent));
  const style = CATEGORY_STYLES[task.category];

  return (
    <View style={styles.taskCard}>
      <View
        style={[
          styles.taskLeftBar,
          {
            backgroundColor: style.accent,
          },
        ]}
      />
      <View style={styles.taskCardInner}>
        <View style={styles.taskCardHeaderRow}>
          <View style={{ flexDirection: "row", alignItems: "center" }}>
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
          <View style={styles.taskProgressBox}>
            <Text style={styles.taskProgressText}>{progress}%</Text>
            <View style={styles.taskProgressBarBg}>
              <View
                style={[
                  styles.taskProgressBarFill,
                  { backgroundColor: style.accent, width: `${progress}%` },
                ]}
              />
            </View>
          </View>
        </View>
        <Text style={styles.taskTitle} numberOfLines={1}>
          {task.title}
        </Text>
        <Text style={styles.taskStatusLine}>
          {statusText}
          {timeText}
        </Text>
      </View>
    </View>
  );
}

/* =========================================================
 *   캘린더 뷰
 * =======================================================*/

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

  const monthLabel = `${year}.${String(monthIndex + 1).padStart(2, "0")}`;
  const weekdays = ["일", "월", "화", "수", "목", "금", "토"];

  return (
    <View style={styles.calendarRoot}>
      <View style={styles.calendarHeader}>
        <TouchableOpacity
          onPress={goPrev}
          activeOpacity={0.8}
          style={styles.calendarArrowBtn}
        >
          <Text>◀</Text>
        </TouchableOpacity>
        <Text style={styles.calendarMonthLabel}>{monthLabel}</Text>
        <TouchableOpacity
          onPress={goNext}
          activeOpacity={0.8}
          style={styles.calendarArrowBtn}
        >
          <Text>▶</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.calendarWeekRow}>
        {weekdays.map((w) => (
          <Text key={w} style={styles.calendarWeekText}>
            {w}
          </Text>
        ))}
      </View>

      <View style={styles.calendarGrid}>
        {cells.map((d, idx) => {
          if (d == null) {
            return <View key={idx} style={styles.calendarCellEmpty} />;
          }

          const iso = toISODate(year, monthIndex + 1, d);
          const isToday =
            d === today && monthIndex === thisMonth && year === thisYear;
          const isSelected = iso === selectedDate;
          const hasTasks = tasks.some((t) => t.date === iso);

          let bg = "#FFFFFF";
          let tx = "#111827";
          if (isSelected) {
            bg = "#020617";
            tx = "#FFFFFF";
          } else if (isToday) {
            bg = "#E5E7EB";
            tx = "#111827";
          }

          return (
            <TouchableOpacity
              key={idx}
              activeOpacity={0.9}
              style={[styles.calendarCell, { backgroundColor: bg }]}
              onPress={() => onSelectDate(iso)}
            >
              <Text style={[styles.calendarCellText, { color: tx }]}>{d}</Text>
              {hasTasks && <View style={styles.calendarDot} />}
            </TouchableOpacity>
          );
        })}
      </View>

      <Text style={styles.calendarHint}>
        ● 초록 점이 있는 날짜는 Task가 있는 날입니다. 날짜를 탭하면 해당
        날짜 Stack 화면으로 이동합니다.
      </Text>
    </View>
  );
}

/* =========================================================
 *   루틴 뷰
 * =======================================================*/

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
      style={{ flex: 1 }}
      contentContainerStyle={styles.routineRoot}
    >
      {groups.map((g) => {
        const style =
          CATEGORY_STYLES[g.category === "여행" ? "여행" : "루틴"];
        return (
          <View key={g.id} style={styles.routineCard}>
            <View style={styles.routineHeaderRow}>
              <View style={{ flex: 1 }}>
                <View style={styles.routineBadgeRow}>
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
                  <Text style={styles.routineDesc} numberOfLines={2}>
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
              <TouchableOpacity
                style={styles.routineDetailBtn}
                activeOpacity={0.85}
              >
                <Text style={styles.routineDetailText}>상세 보기</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={[
                  styles.routineApplyBtn,
                  { backgroundColor: "#020617", borderColor: "#020617" },
                ]}
                activeOpacity={0.9}
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
          <Text style={styles.emptyTitle}>아직 저장된 루틴이 없어요.</Text>
          <Text style={styles.emptyDesc}>
            오늘 작성한 Task 묶음을 루틴으로 저장해보세요.
          </Text>
        </View>
      )}
    </ScrollView>
  );
}

/* =========================================================
 *   Task 편집 모달
 * =======================================================*/

type TaskEditSheetProps = {
  task: Task;
  onClose: () => void;
  onSave: (task: Task) => void;
};

function TaskEditSheet({ task, onClose, onSave }: TaskEditSheetProps) {
  const [title, setTitle] = useState(task.title);
  const [category, setCategory] =
    useState<CategoryFilter>(task.category);
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
    <Modal transparent animationType="fade">
      <TouchableOpacity
        activeOpacity={1}
        style={styles.modalBackdrop}
        onPress={onClose}
      >
        <View
          style={styles.modalCard}
          onStartShouldSetResponder={() => true}
          onTouchEnd={(e) => e.stopPropagation()}
        >
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
            style={{ maxHeight: 280 }}
            contentContainerStyle={{ paddingBottom: 8 }}
          >
            <View style={styles.modalField}>
              <Text style={styles.modalLabel}>제목</Text>
              <TextInput
                style={styles.modalInput}
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
                      activeOpacity={0.9}
                      style={[
                        styles.modalCategoryChip,
                        selected
                          ? { backgroundColor: s.accent, borderColor: "transparent" }
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

            <View style={[styles.modalField, { flexDirection: "row", justifyContent: "space-between", alignItems: "center" }]}>
              <View style={{ flexDirection: "row", alignItems: "center" }}>
                <Switch
                  value={hasTime}
                  onValueChange={setHasTime}
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
              <Slider
                minimumValue={0}
                maximumValue={100}
                step={1}
                value={progress}
                onValueChange={(v) => setProgress(Math.round(v))}
              />
            </View>

            <View style={[styles.modalField, { flexDirection: "row", justifyContent: "space-between", alignItems: "center" }]}>
              <View style={{ flexDirection: "row", alignItems: "center" }}>
                <Switch
                  value={pinned}
                  onValueChange={setPinned}
                />
                <Text style={styles.modalSwitchLabel}>상단 고정</Text>
              </View>
              <View style={styles.modalStatusToggle}>
                <TouchableOpacity
                  onPress={() => setStatus("TODO")}
                  style={[
                    styles.modalStatusBtn,
                    status === "TODO" && {
                      backgroundColor: style.accent,
                    },
                  ]}
                >
                  <Text
                    style={[
                      styles.modalStatusBtnText,
                      status === "TODO" && { color: "#FFFFFF" },
                    ]}
                  >
                    진행 중
                  </Text>
                </TouchableOpacity>
                <TouchableOpacity
                  onPress={() => setStatus("DONE")}
                  style={[
                    styles.modalStatusBtn,
                    status === "DONE" && {
                      backgroundColor: style.accent,
                    },
                  ]}
                >
                  <Text
                    style={[
                      styles.modalStatusBtnText,
                      status === "DONE" && { color: "#FFFFFF" },
                    ]}
                  >
                    완료
                  </Text>
                </TouchableOpacity>
              </View>
            </View>
          </ScrollView>

          <View style={styles.modalFooterRow}>
            <TouchableOpacity
              onPress={onClose}
              style={styles.modalCancelBtn}
            >
              <Text style={styles.modalCancelText}>취소</Text>
            </TouchableOpacity>
            <TouchableOpacity
              onPress={handleSave}
              style={[styles.modalSaveBtn, { backgroundColor: style.accent }]}
            >
              <Text style={styles.modalSaveText}>저장</Text>
            </TouchableOpacity>
          </View>
        </View>
      </TouchableOpacity>
    </Modal>
  );
}

/* =========================================================
 *   스타일
 * =======================================================*/

const styles = StyleSheet.create({
  root: {
    flex: 1,
    backgroundColor: "#020617",
    alignItems: "center",
    justifyContent: "center",
  },
  phoneFrame: {
    width: 390,
    height: 800,
    backgroundColor: "#F1F5F9",
    borderRadius: 32,
    overflow: "hidden",
    shadowColor: "#000",
    shadowOpacity: 0.25,
    shadowOffset: { width: 0, height: 10 },
    shadowRadius: 20,
    elevation: 10,
  },

  header: {
    backgroundColor: "#FFFFFF",
    borderBottomWidth: 1,
    borderBottomColor: "#E2E8F0",
    paddingHorizontal: 16,
    paddingTop: Platform.OS === "ios" ? 12 : 10,
    paddingBottom: 10,
  },
  headerTopRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 6,
  },
  headerTitleBlock: {
    flexDirection: "column",
  },
  headerStackLabel: {
    fontSize: 11,
    color: "#94A3B8",
  },
  headerSubLabel: {
    fontSize: 11,
    color: "#64748B",
    marginTop: 2,
  },
  avatarButton: {
    width: 32,
    height: 32,
    borderRadius: 16,
    backgroundColor: "#F1F5F9",
    alignItems: "center",
    justifyContent: "center",
  },
  avatarEmoji: {
    fontSize: 16,
  },
  workspaceRow: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    marginTop: 4,
  },
  workspaceButton: {
    flexDirection: "row",
    alignItems: "center",
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 999,
    borderWidth: 1,
    borderColor: "#E2E8F0",
    backgroundColor: "#F8FAFC",
    maxWidth: 220,
  },
  workspaceText: {
    fontSize: 11,
    color: "#475569",
    flexShrink: 1,
  },
  workspaceArrow: {
    fontSize: 9,
    color: "#94A3B8",
    marginLeft: 4,
  },
  workspaceHint: {
    fontSize: 9,
    color: "#CBD5F5",
  },

  categoryScroll: {
    paddingTop: 8,
    paddingBottom: 4,
    gap: 8,
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

  todayContent: {
    paddingHorizontal: 16,
    paddingTop: 12,
    paddingBottom: 80,
  },

  emptyBox: {
    alignItems: "center",
    justifyContent: "center",
    marginTop: 80,
  },
  emptyTitle: {
    fontSize: 13,
    color: "#94A3B8",
  },
  emptyDesc: {
    marginTop: 4,
    fontSize: 11,
    color: "#A0AEC0",
  },

  addDateButton: {
    marginTop: 8,
    width: "100%",
    paddingVertical: 10,
    borderRadius: 24,
    borderWidth: 1,
    borderStyle: "dashed",
    borderColor: "#CBD5F5",
    backgroundColor: "#E5E7EB60",
    alignItems: "center",
    justifyContent: "center",
  },
  addDateButtonText: {
    fontSize: 12,
    color: "#64748B",
  },

  tabBar: {
    height: 56,
    backgroundColor: "#FFFFFF",
    borderTopWidth: 1,
    borderTopColor: "#E2E8F0",
    flexDirection: "row",
  },
  tabButton: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
  },
  tabIcon: {
    fontSize: 16,
    marginBottom: 2,
    color: "#94A3B8",
  },
  tabLabel: {
    fontSize: 11,
    color: "#94A3B8",
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
    right: 24,
    bottom: 70,
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: "#020617",
    alignItems: "center",
    justifyContent: "center",
    shadowColor: "#000",
    shadowOpacity: 0.25,
    shadowOffset: { width: 0, height: 6 },
    shadowRadius: 10,
    elevation: 8,
  },
  fabText: {
    fontSize: 26,
    color: "#FFFFFF",
    marginTop: -2,
  },

  taskRow: {
    height: 80,
    marginBottom: 8,
    position: "relative",
  },
  swipeHintRow: {
    ...StyleSheet.absoluteFillObject,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    paddingHorizontal: 16,
  },
  swipeLeftBg: {
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 999,
    backgroundColor: "rgba(16,185,129,0.9)",
  },
  swipeRightBg: {
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 999,
    backgroundColor: "rgba(239,68,68,0.9)",
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
    flexDirection: "row",
    paddingHorizontal: 16,
    paddingVertical: 10,
    borderWidth: 1,
    borderColor: "#E5E7EB",
    shadowColor: "#000",
    shadowOpacity: 0.05,
    shadowRadius: 8,
    shadowOffset: { width: 0, height: 2 },
  },
  taskLeftBar: {
    width: 4,
    borderRadius: 999,
    marginRight: 8,
  },
  taskCardInner: {
    flex: 1,
  },
  taskCardHeaderRow: {
    flexDirection: "row",
    justifyContent: "space-between",
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
    marginRight: 4,
  },
  categoryChipSmallText: {
    fontSize: 10,
  },
  taskProgressBox: {
    alignItems: "flex-end",
  },
  taskProgressText: {
    fontSize: 10,
    color: "#94A3B8",
    marginBottom: 2,
  },
  taskProgressBarBg: {
    width: 56,
    height: 6,
    borderRadius: 999,
    backgroundColor: "#E2E8F0",
    overflow: "hidden",
  },
  taskProgressBarFill: {
    height: "100%",
  },
  taskTitle: {
    fontSize: 13,
    fontWeight: "600",
    color: "#111827",
    marginTop: 2,
  },
  taskStatusLine: {
    fontSize: 11,
    color: "#6B7280",
    marginTop: 4,
  },

  calendarRoot: {
    flex: 1,
    paddingHorizontal: 16,
    paddingTop: 12,
    paddingBottom: 16,
  },
  calendarHeader: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    marginBottom: 8,
  },
  calendarArrowBtn: {
    width: 32,
    height: 32,
    borderRadius: 16,
    backgroundColor: "#E5E7EB",
    alignItems: "center",
    justifyContent: "center",
  },
  calendarMonthLabel: {
    fontSize: 14,
    fontWeight: "600",
    color: "#111827",
  },
  calendarWeekRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 4,
  },
  calendarWeekText: {
    flex: 1,
    textAlign: "center",
    fontSize: 10,
    color: "#6B7280",
  },
  calendarGrid: {
    flexDirection: "row",
    flexWrap: "wrap",
  },
  calendarCell: {
    width: `${100 / 7}%`,
    aspectRatio: 1.1,
    borderRadius: 12,
    alignItems: "center",
    justifyContent: "center",
    marginBottom: 4,
  },
  calendarCellEmpty: {
    width: `${100 / 7}%`,
    aspectRatio: 1.1,
    marginBottom: 4,
  },
  calendarCellText: {
    fontSize: 11,
  },
  calendarDot: {
    marginTop: 2,
    width: 6,
    height: 6,
    borderRadius: 3,
    backgroundColor: "#10B981",
  },
  calendarHint: {
    marginTop: 8,
    fontSize: 10,
    color: "#9CA3AF",
  },

  routineRoot: {
    paddingHorizontal: 16,
    paddingTop: 12,
    paddingBottom: 60,
  },
  routineCard: {
    backgroundColor: "#FFFFFF",
    borderRadius: 24,
    borderWidth: 1,
    borderColor: "#E5E7EB",
    paddingHorizontal: 16,
    paddingVertical: 12,
    marginBottom: 10,
  },
  routineHeaderRow: {
    flexDirection: "row",
    justifyContent: "space-between",
  },
  routineBadgeRow: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 4,
  },
  routineTaskCount: {
    fontSize: 9,
    color: "#9CA3AF",
    marginLeft: 4,
  },
  routineTitle: {
    fontSize: 13,
    fontWeight: "600",
    color: "#111827",
  },
  routineDesc: {
    fontSize: 10,
    color: "#6B7280",
    marginTop: 4,
  },
  routineTaskList: {
    marginTop: 4,
  },
  routineTaskRow: {
    flexDirection: "row",
    alignItems: "center",
    marginTop: 2,
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
    color: "#4B5563",
    flexShrink: 1,
  },
  routineFooterRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginTop: 10,
  },
  routineDetailBtn: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 999,
    borderWidth: 1,
    borderColor: "#E2E8F0",
    backgroundColor: "#F8FAFC",
  },
  routineDetailText: {
    fontSize: 10,
    color: "#475569",
  },
  routineApplyBtn: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 999,
    borderWidth: 1,
  },
  routineApplyText: {
    fontSize: 10,
    color: "#FFFFFF",
  },

  modalBackdrop: {
    flex: 1,
    backgroundColor: "rgba(0,0,0,0.3)",
    alignItems: "center",
    justifyContent: "center",
    paddingHorizontal: 16,
  },
  modalCard: {
    width: "100%",
    maxWidth: 340,
    backgroundColor: "#FFFFFF",
    borderRadius: 24,
    paddingHorizontal: 16,
    paddingBottom: 14,
    paddingTop: 10,
  },
  modalHandle: {
    alignSelf: "center",
    width: 40,
    height: 4,
    borderRadius: 999,
    backgroundColor: "#E5E7EB",
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
    marginBottom: 8,
  },
  modalField: {
    marginBottom: 10,
  },
  modalLabel: {
    fontSize: 10,
    color: "#6B7280",
    marginBottom: 4,
  },
  modalInput: {
    borderWidth: 1,
    borderColor: "#E2E8F0",
    borderRadius: 16,
    paddingHorizontal: 12,
    paddingVertical: 8,
    fontSize: 11,
    backgroundColor: "#F8FAFC",
  },
  modalCategoryRow: {
    flexDirection: "row",
    flexWrap: "wrap",
    gap: 6,
  },
  modalCategoryChip: {
    paddingHorizontal: 10,
    paddingVertical: 6,
    borderRadius: 999,
    borderWidth: 1,
  },
  modalCategoryChipText: {
    fontSize: 10,
  },
  modalSwitchLabel: {
    fontSize: 10,
    color: "#6B7280",
    marginLeft: 6,
  },
  modalTimeInput: {
    borderWidth: 1,
    borderColor: "#E2E8F0",
    borderRadius: 12,
    paddingHorizontal: 8,
    paddingVertical: 4,
    fontSize: 11,
    backgroundColor: "#F8FAFC",
    minWidth: 70,
    textAlign: "center",
  },
  modalStatusToggle: {
    flexDirection: "row",
    backgroundColor: "#F8FAFC",
    borderRadius: 999,
    padding: 2,
  },
  modalStatusBtn: {
    paddingHorizontal: 10,
    paddingVertical: 6,
    borderRadius: 999,
  },
  modalStatusBtnText: {
    fontSize: 10,
    color: "#6B7280",
  },
  modalFooterRow: {
    flexDirection: "row",
    justifyContent: "flex-end",
    marginTop: 8,
    gap: 8,
  },
  modalCancelBtn: {
    paddingHorizontal: 12,
    paddingVertical: 8,
    borderRadius: 999,
    borderWidth: 1,
    borderColor: "#E2E8F0",
    backgroundColor: "#F8FAFC",
  },
  modalCancelText: {
    fontSize: 11,
    color: "#4B5563",
  },
  modalSaveBtn: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 999,
  },
  modalSaveText: {
    fontSize: 11,
    color: "#FFFFFF",
  },
});

export default StackTodoScreen;