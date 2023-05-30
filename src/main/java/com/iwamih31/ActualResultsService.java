package com.iwamih31;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActualResultsService {

	// リポジトリ格納の為のプライベートフィールド
	@Autowired
	private OfficeRepository officeRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PlanRepository planRepository;


	public List<User> user_All() {
		return userRepository.findAll();
	}

	public List<Office> office_All() {
		if (next_Office_Id() == 1) set_Office();
		return officeRepository.findAll();
	}

	public List<User> user_List() {
		return userRepository.user_List();
	}

	public User user(int id) {
		return userRepository.getReferenceById(id);
	}

	public Plan plan(int id) {
		return planRepository.getReferenceById(id);
	}

	public boolean exists_Plan(int id) {
		return planRepository.existsById(id);
	}

	public Office office(int id) {
		return officeRepository.getReferenceById(id);
	}

	public String user_Insert(User user, int id) {
		__consoleOut__("user_Insert開始");
		user.setId(id);
		String message = "ID = " + user.getId() + " の利用者データ";
		try {
			userRepository.save(user);
			message += " を登録しました";
		} catch (Exception e) {
			message += "登録に失敗しました " + e.getMessage();
		}
		__consoleOut__("user_Insert終了");
		return message;
	}

	public String plan_Insert(Plan plan, int user_id, String year_month, String[] day_of_weeks) {
		__consoleOut__("public String plan_Insert(Plan plan, int user_id)開始");
		int next_Plan_Id = next_Plan_Id();
		String message = "ID = " + next_Plan_Id + " の利用者データ";
		try {
			LocalDate localDate = to_LocalDate(year_month);
			plan.setStart_date(plan_Start_date(user_id, localDate));
			plan.setLast_date(plan_last_date(user_id, localDate));
			plan.setDay_of_week(plan_day_of_week(day_of_weeks));
			plan.setMinutes(plan_Minute(plan.getSubject()));
			__consoleOut__("plan = " + plan);
			planRepository.save(plan);
			message += " を登録しました";
		} catch (Exception e) {
			message += "登録に失敗しました " + e.getMessage();
		}
		__consoleOut__("public String plan_Insert(Plan plan, int user_id)終了");
		return message;
	}

	public String office_Insert(Office office, int id) {
		__consoleOut__("office_Insert開始");
		office.setId(id);
		String message = "ID = " + office.getId() + " の事業所データ";
		try {
			officeRepository.save(office);
			message += " を登録しました";
		} catch (Exception e) {
			message += "登録に失敗しました " + e.getMessage();
		}
		__consoleOut__("office_Insert終了");
		return message;
	}

	public String[] office_Item_Names() {
		String[] item_Names =  {"事業所名","部署名"};
		return item_Names;
	}

	public List<Office> office_Report() {
		return officeRepository.findAll();
	}

	public String user_Update(User user, int id) {
		__consoleOut__("user_Update開始");
		user.setId(id);
		String message = "ID = " + user.getId() + " の利用者データ";
		try {
			userRepository.save(user);
			message += " を更新しました";
		} catch (Exception e) {
			message += "更新に失敗しました " + e.getMessage();
		}
		__consoleOut__("user_Update終了");
		return message;
	}

	public String office_Update(Office office, int id) {
		__consoleOut__("office_Update開始");
		office.setId(id);
		String message = "ID = " + office.getId() + " の事業所データ";
		try {
			officeRepository.save(office);
			message += " を更新しました";
		} catch (Exception e) {
			message += "更新に失敗しました " + e.getMessage();
		}
		__consoleOut__("office_Update終了");
		return message;
	}

	public String plan_Update(Plan plan, String[] day_of_weeks) {
		__consoleOut__("event_Update開始 " +  plan);
		String message = "ID = " + plan.getId() + " の訪問計画";
		try {
			plan.setDay_of_week(plan_day_of_week(day_of_weeks));
			plan.setLast_date(plan_last_date(plan.getUser_id(), plan.getStart_date()));
			plan.setMinutes(plan_Minute(plan.getSubject()));
			planRepository.save(plan);
			message += "を更新しました";
		} catch (Exception e) {
			message += "更新に失敗しました " + e.getMessage();
		}
		__consoleOut__("event_Update終了");
		return message;
	}

	// フォームで受け取った曜日の配列を " " を挿んで連結
	private String plan_day_of_week(String[] day_of_weeks) {
		String plan_day_of_week = "";
		for (String day_of_week : day_of_weeks) {
			plan_day_of_week += day_of_week + " ";
		}
		// 前後のスペースを取る
		return plan_day_of_week.trim();
	}

	private LocalDate plan_last_date(int user_id, LocalDate localDate) {
		LocalDate plan_last_date = null;
		List<Plan> after_Plan_List = planRepository.after_Plan_List(user_id, localDate);
		// 指定月以降にプランがある場合
		if(after_Plan_List.size() > 0) {
		// plan_last_date は次のプランの開始月の前の月
			plan_last_date = after_Plan_List.get(0).getStart_date().minusMonths(1);
		// 指定月以降にプランが無い場合
		} else {
			// plan_last_date は開始月の1年後
			plan_last_date = LocalDate.now().plusYears(1);
		}
		return plan_last_date;
	}

	private LocalDate plan_Start_date(int user_id, LocalDate localDate) {
		LocalDate plan_Start_date = localDate;
		List<Plan> before_Plan_List = planRepository.before_Plan_List(user_id, localDate);
		// 指定月以前にプランがある場合のみ
		if(before_Plan_List.size() > 0) {
			// 直前のプランの終了月を全て localDate.minusMonths(1) に書き換え
			LocalDate previous_Plan_Start = before_Plan_List.get(0).getStart_date();
			List<Plan> plan_List = planRepository.get_plan_List(user_id, previous_Plan_Start);
			for (Plan plan : plan_List) {
				plan.setLast_date(localDate.minusMonths(1));
				planRepository.save(plan);
			}
			plan_Start_date = localDate;
		}
		return plan_Start_date;
	}

	private Integer plan_Minute(String subject) {
		switch (subject) {
			case "身体01":
				return 10;
			case "身体１":
				return 20;
			case "身体２":
				return 40;
			case "身体３":
				return 60;
			case "身体４":
				return 80;
			case "身体５":
				return 100;
			case "生活１":
				return 10;
			case "生活２":
				return 10;
			case "生活３":
				return 10;
			case "生活４":
				return 10;
			case "生活５":
				return 10;
			default	:
				return 0;
		}
	}

	/** 事業所データをExcelファイルとして出力 */
	public String office_Output_Excel(HttpServletResponse response) {
		__consoleOut__("office_Output_Excel(HttpServletResponse response) 開始");
		Excel excel = new Excel();
		String message = null;
		String[] column_Names = Set.get_Name_Set(LabelSet.officeReport_Set);
		int[] column_Width = Set.get_Value_Set(LabelSet.officeReport_Set);
		List<Office> office_Report = office_Report();
		String[][] table_Data = new String[office_Report.size()][];
		for (int i = 0; i < table_Data.length; i++) {
			Office office = office_Report.get(i);
			table_Data[i] = new String[] {
					String.valueOf(office.getId()),
					String.valueOf(office.getItem_name()),
					String.valueOf(office.getItem_value())
				};
		}
		message = excel.output_Excel("事業所", column_Names, column_Width, table_Data, response);
		__consoleOut__("office_Output_Excel(HttpServletResponse response) 終了");
		return message;
	}

	/** 利用者情報一覧をExcelファイルとして出力 */
	public String userList_Output_Excel(HttpServletResponse response) {
		__consoleOut__("user_Output_Excel(int user_id, String year_month, HttpServletResponse response) 開始");
		Excel excel = new Excel();
		String message = null;
		int[] column_Width = Set.get_Value_Set(LabelSet.user_Set);
		String[][] output_Data = to_Array(userList_Sheet());
		WorkSheet workSheet = new UserListWorkSheet("利用者情報一覧", column_Width, output_Data);
		message = excel.output_Excel_Sheet("利用者情報一覧", workSheet, response);
		__consoleOut__("user_Output_Excel(int user_id, String year_month, HttpServletResponse response) 終了");
		return message;
	}

	/** 訪問予定計画表（個人）をExcelファイルとして出力 */
	public String plan_Output_Excel(int user_id, String year_month, HttpServletResponse response) {
		__consoleOut__("plan_Output_Excel(int user_id, String year_month, HttpServletResponse response) 開始");
		Excel excel = new Excel();
		String message = null;
		int[] column_Width = Set.get_Value_Set(LabelSet.plan_Set);
		String[][] output_Data = to_Array(plan_To_Visit_Sheet(user_id, year_month));
		String sheet_Name = user(user_id).getName();
		if(sheet_Name == null || sheet_Name == "") sheet_Name = "訪問予定計画表（個人）";
		WorkSheet workSheet = new PlanWorkSheet(sheet_Name, column_Width, output_Data);
		message = excel.output_Excel_Sheet("訪問予定計画表（個人）", workSheet, response);
		__consoleOut__("plan_Output_Excel(int user_id, String year_month, HttpServletResponse response) 終了");
		return message;
	}

	/** 実績記入表（個人）をExcelファイルとして出力 */
	public String user_Output_Excel(int user_id, String year_month, HttpServletResponse response) {
		__consoleOut__("user_Output_Excel(int user_id, String year_month, HttpServletResponse response) 開始");
		Excel excel = new Excel();
		String message = null;
		int[] column_Width = Set.get_Value_Set(LabelSet.actualResults_Set);
		String[][] output_Data = to_Array(actualResults_Sheet(user_id, year_month));
		String sheet_Name = user(user_id).getName();
		if(sheet_Name == null || sheet_Name == "") sheet_Name = "実績記入表（個人）";
		WorkSheet workSheet = new ActualResultsWorkSheet(sheet_Name, column_Width, output_Data);
		message = excel.output_Excel_Sheet("実績記入表（個人）", workSheet, response);
		__consoleOut__("user_Output_Excel(int user_id, String year_month, HttpServletResponse response) 終了");
		return message;
	}

	/** 実績記入表（全利用者分）をExcelファイルとして出力 */
	public String actualResults_Output_Excel(String year_month, HttpServletResponse response) {
		__consoleOut__("office_Output_Excel(HttpServletResponse response) 開始");
		Excel excel = new Excel();
		String message = null;
		List<WorkSheet> workSheets = new ArrayList<>();
		int[] column_Width = Set.get_Value_Set(LabelSet.actualResults_Set);
		List<User> user_List = user_List();
		for (User user : user_List) {
			String[][] output_Data = to_Array(actualResults_Sheet(user.getId(), year_month));
			WorkSheet workSheet = new ActualResultsWorkSheet(user.getName(), column_Width, output_Data);
			workSheets.add(workSheet);
		}
		message = excel.output_Excel_Sheets("実績記入表", workSheets, response);
		__consoleOut__("office_Output_Excel(HttpServletResponse response) 終了");
		return message;
	}

	/** Excelシート（利用者情報一覧）作成用値データ */
	public List<String[]> userList_Sheet() {
		__consoleOut__("public List<String[]> actualResults_Sheet(int user_id, String year_month)開始");
		// 列追加用リスト作成
		List<String[]> sheet_Array = new ArrayList<>();
		// ヘッド部分追加
		sheet_Array.addAll(head_Row_Values(userList_head_Rows()));
		// ラベル行追加
		sheet_Array.add(userList_Labels());
		// データ行追加
		sheet_Array.addAll(data_Row_Values_userList());
		__consoleOut__("public List<String[]> actualResults_Sheet(int user_id, String year_month)終了");
		return sheet_Array;
	}

	private String[] head_Row_Values_userList() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	/** Excelシート（実績）作成用値データ */
	public List<String[]> actualResults_Sheet(int user_id, String year_month) {
		__consoleOut__("public List<String[]> actualResults_Sheet(int user_id, String year_month)開始");
		// 列追加用リスト作成
		List<String[]> sheet_Array = new ArrayList<>();
		// ヘッド部分追加
		sheet_Array.addAll(head_Row_Values(head_Rows(user_id, year_month)));
		// ラベル行追加
		sheet_Array.add(actualResults_Labels());
		// データ行追加
		sheet_Array.addAll(data_Row_Values_actualResults(user_id, year_month));
		__consoleOut__("public List<String[]> actualResults_Sheet(int user_id, String year_month)終了");
		return sheet_Array;
	}

	/** Excelシート（予定）作成用値データ */
	public List<String[]> plan_To_Visit_Sheet(int user_id, String year_month) {
		__consoleOut__("public List<String[]> plan_To_Visit_Sheet(int user_id, String year_month)開始");
		// 列追加用リスト作成
		List<String[]> sheet_Array = new ArrayList<>();
		// ヘッド部分追加
		sheet_Array.addAll(head_Row_Values(head_Rows(user_id, year_month)));
		// ラベル行追加
		sheet_Array.add(plan_Labels());
		// データ行追加
		sheet_Array.addAll(data_Row_Values_Plan_To_Visit(user_id, year_month));
		__consoleOut__("public List<String[]> plan_To_Visit_Sheet(int user_id, String year_month)終了");
		return sheet_Array;
	}

	private String[][] to_Array(List<String[]> list) {
		// Listから2次配列へ変換
		String[][] array = new String[list.size()][];
		for (int i = 0; i < array.length; i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	/** ヘッド部分 */
	private List<String[]> head_Row_Values(String[][] head_Rows) {
		List<String[]> head_Row_Values = new ArrayList<>();
		for (int i = 0; i < head_Rows.length; i++) {
			// i が奇数（valueの配列）の時だけ処理を行う
			if (i % 2 != 0) head_Row_Values.add(head_Rows[i]);
		}
		return head_Row_Values;
	}

	/** 表部分 ラベル行 */
	String[] actualResults_Labels() {
		return Set.get_Name_Set(LabelSet.actualResults_Set);
	}

	/** 表部分 ラベル行 */
	String[] userList_Labels() {
		return Set.get_Name_Set(LabelSet.user_Set);
	}

	/** 表部分 データ行（実績） */
	List<String[]> data_Row_Values_actualResults(int user_id, String year_month) {
		// データ格納用リスト作成
		List<String[]> data_Row_Values = new ArrayList<>();
		// 月の日数を取得
		List<LocalDate> year_month_dates = year_month_dates(year_month);
		// 日数分ループ
		for (LocalDate localDate : year_month_dates) {
			// localDate の曜日を日本語の文字列に変換
			String day_of_week = day_of_week(localDate);
			// 期間内の同じ曜日のプランを List<Plan> として取得
			List<Plan> plan_List = get_Plan_List(user_id, to_LocalDate(year_month), day_of_week);
			// 該当プランがあれば
			if(plan_List.size() > 0) {
				// プラン数分ループ
				for (Plan plan : plan_List) {
					// プランに応じた行を作成
					data_Row_Values.add(actualResults_Row(localDate, plan));
				}
			// プランが無ければ
			} else {
				// プラン無しの行を作成
				data_Row_Values.add(actualResults_Row(localDate, null));
			}
		}
		return data_Row_Values;
	}

	/** 表部分 データ行（予定） */
	List<String[]> data_Row_Values_Plan_To_Visit(int user_id, String year_month) {
		// データ格納用リスト作成
		List<String[]> data_Row_Values = new ArrayList<>();
		// 期間内のプランを取得
		List<Plan> plan_List = plan_List(user_id, to_LocalDate(year_month));
		// 該当プランがあれば
		if(plan_List.size() > 0) {
			// プラン数分ループ
			for (Plan plan : plan_List) {
				// プランに応じた行を作成
				data_Row_Values.add(plan_To_Visit_Row(plan));
			}
			// プランが無ければ
		} else {
			// プラン無しの行を作成
			data_Row_Values.add(plan_To_Visit_Row(null));
		}
		return data_Row_Values;
	}

	/** 表部分 データ行（予定） */
	List<String[]> data_Row_Values_userList() {
		// データ格納用リスト作成
		List<String[]> data_Row_Values = new ArrayList<>();
		// ユーザ一覧を取得
		List<User> user_List = user_All();
		// 該当プランがあれば
		if(user_List.size() > 0) {
			// プラン数分ループ
			for (User user : user_List) {
				// プランに応じた行を作成
				data_Row_Values.add(user_List_Row(user));
			}
			// プランが無ければ
		} else {
			// プラン無しの行を作成
			data_Row_Values.add(user_List_Row(null));
		}
		return data_Row_Values;
	}

	private List<Plan> get_Plan_List(int user_id, LocalDate localDate, String day_of_week) {
		return planRepository.get_Plan_List(user_id, localDate, day_of_week);
	}

	private String[][] head_Rows(int user_id, String year_month) {
		__consoleOut__("private String[][] head_Rows(int user_id, String year_month)開始");
  	User user = user(user_id);
  	Integer[] split_Date = split_Date(year_month);
  	String y_m_ = split_Date[0] + " 年　　" + split_Date[1] + " 月";
  	String fase = "　氏名　";
  	String name = user.getRoom() +  "　" + user.getName() + "　様";
		String form = "訪問介護サービス提供実施記録票";
		String com_ = office_item_value("事業所名");
		// String[偶数][] = セルの罫線, String[奇数][] = セルの値
  	String[][] head_Rows = {
  			{"  ","  ","  ","  ","  ","  ","  ","  "},
  			{""  ,y_m_,""  ,""  ,form,""  ,""  ,""  },
  			{"匚","二","二","コ","匚","二","二","コ"},
  			{fase,name,""  ,""  ,""  ,com_,""  ,""  },
  			{"  ","  ","  ","  ","  ","  ","  ","  "},
  			{""  ,""  ,""  ,""  ,""  ,""  ,""  ,""  }
  	};
		__consoleOut__("private String[][] head_Rows(int user_id, String year_month)終了");
		__consoleOut__("String[][] head_Rows = " + head_Rows);
		return head_Rows;
	}

	private String[][] userList_head_Rows() {
		__consoleOut__("private String[][] head_Rows(int user_id, String year_month)開始");
		String form = "利用者情報一覧";
		String com_ = office_item_value("事業所名");
		String depa = office_item_value("部署名");
		// String[偶数][] = セルの罫線, String[奇数][] = セルの値
		String[][] head_Rows = {
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{""  ,""  ,""  ,""  ,form,""  ,""  ,""  },
				{"匚","二","二","コ","匚","二","二","コ"},
				{""  ,com_,""  ,""  ,""  ,depa,""  ,""  },
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{""  ,""  ,""  ,""  ,""  ,""  ,""  ,""  }
		};
		__consoleOut__("private String[][] head_Rows(int user_id, String year_month)終了");
		__consoleOut__("String[][] head_Rows = " + head_Rows);
		return head_Rows;
	}

	private String office_item_value(String item_name) {
		String value = "";
		List<String> item_value = officeRepository.item_value(item_name);
		if (item_value.size() > 0) value = officeRepository.item_value(item_name).get(0);
		return value;
	}

	private String[] actualResults_Row(LocalDate localDate, Plan plan) {
  	String[] row = new String[] {
  			make_String(month_day_JP(localDate)),
				make_String(day_of_week(localDate)),
				make_String(plan_Subject(plan)),
				make_String(plan_Time(plan)),
				make_String(plan_Items(plan)),
				"",
				"",
				""
			};
		return row;
	}

	private String[] plan_To_Visit_Row(Plan plan) {
		String[] row = new String[] {
				make_String(plan_Subject(plan)),
				make_String(plan_Day_of_week(plan)),
				make_String(plan_Start_time(plan)),
				make_String(plan_Minutes(plan)),
				make_String(plan_Items(plan)),
				make_String(plan_Note(plan))};
		return row;
	}

	private String[] user_List_Row(User user) {
		String[] row = new String[] {
				make_String(user.getId()),
				make_String(user.getRoom()),
				make_String(user.getName()),
				make_String(user.getBirthday()),
				make_String(user.getLevel()),
				make_String(user.getMove_in()),
				make_String(user.getUse()),
				make_String(user.getNote())};
		return row;
	}

	private String plan_Day_of_week(Plan plan) {
		String field_Value = "";
		if (plan != null) field_Value = plan.getDay_of_week();
		return field_Value;
	}

	private String plan_Subject(Plan plan) {
		String field_Value = "";
		if(plan != null) field_Value = plan.getSubject();
		return field_Value;
	}

	private String plan_Start_time(Plan plan) {
		String field_Value = "";
		if (plan != null) field_Value = plan.getStart_time();
		return field_Value;
	}

	private Integer plan_Minutes(Plan plan) {
		Integer field_Value = 0;
		if (plan != null) field_Value = plan.getMinutes();
		return field_Value;
	}

	private String plan_Items(Plan plan) {
  	String field_Value = "";
  	if (plan != null) field_Value = plan.getItems();
		return field_Value;
	}

	private String plan_Note(Plan plan) {
		String field_Value = "";
		if (plan != null) field_Value = plan.getNote();
		return field_Value;
	}

	private String make_String(Object object) {
		String make_String = "";
		if (object != null) make_String = String.valueOf(object);
		return make_String;
	}

	private String plan_Time(Plan plan) {
		String plan_Time = "";
		if(plan != null) {
			String start_time = plan.getStart_time();
			Integer minutes = plan.getMinutes();
			if (minutes > 0) {
				LocalTime end_time = to_LocalTime(start_time).plusMinutes(minutes);
				plan_Time = start_time + "～" + end_time;
			}
		}
		return plan_Time;
	}

	private LocalTime to_LocalTime(String start_time) {
		String[] split_Time = split_Time(start_time);
		int hour = Integer.parseInt(split_Time[0]);
		int minute = Integer.parseInt(split_Time[1]);
		return LocalTime.of(hour, minute);
	}

	/** start から end まで add ずつ増やした数を代入した配列を作成 */
  public static Integer[] nums(int start, int end, int add) {
		Integer[] nums = new Integer[(end - start) / add + 1];
		for (int i = 0; i < nums.length; i++) {
			nums[i] = i * add + start;
		}
		return nums;
	}

	public static String[] days(String month) {
		int lastDay = 31;
		switch(month) {
			case "2":
				lastDay = 29;
				break;
			case "4":
			case "6":
			case "9":
			case "11":
				lastDay = 30;
				break;
		}
		Integer[] days = nums(1, lastDay, 1);
		return arrayAlignment(days, 2, "0");
	}

	private String day_of_week(LocalDate localDate) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("E", Locale.JAPANESE);
		return dateTimeFormatter.format(localDate);
	}

	/** String date の 月の日付のListを List<LocalDate> にして返す */
	private List<LocalDate> year_month_dates(String date) {
		__consoleOut__("private List<LocalDate> year_month_dates(String date)開始 " + date);
		List<LocalDate> year_month_dates = new ArrayList<>();
		Integer[] split_Date = split_Date(date);
		int year = split_Date[0];
		int month = split_Date[１];
		LocalDate localDate = to_LocalDate(year, month, 1);
		Month date_Month = localDate.getMonth();
		while (true) {
			year_month_dates.add(localDate);
			localDate = localDate.plusDays(1);
			if(localDate.getMonth() != date_Month ) {
				__consoleOut__("Last date = " + localDate.toString());
				break;
			}
		}
		__consoleOut__("private List<LocalDate> year_month_dates(String date)開始 ");
		return year_month_dates;
	}

	/** localDateTimeを "y年M月" 形式の String に変換 */
	private String year_month_JP(LocalDateTime localDateTime) {
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("y年M月");
		return dateTimeFormatter.format(localDateTime);
	}

	/** localDateTimeを "M月d日" 形式の String に変換 */
	private String month_day_JP(LocalDate localDate) {
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M月d日");
		return dateTimeFormatter.format(localDate);
	}

	private Integer[] split_Date(String date) {
		Integer[] split_Date = null;
		LocalDateTime localDateTime = LocalDateTime.now();
		if (date != null) {
			int year = localDateTime.getYear();
			int month = localDateTime.getMonthValue();
			int day = 1;
			date = date.trim();
			date = date.split(" ")[0];
			date = date.split("T")[0];
			date = date.replace("/", "-");
			String[] array = date.split("-");
			if(array.length > 0 && is_Int(array[0])) year = Integer.parseInt(array[0]);
			if(array.length > 1) month = Integer.parseInt(array[１]);
			if(array.length > 2) day = Integer.parseInt(array[2]);
			split_Date = new Integer[] {year, month, day};
		}
		return split_Date;
	}

	private String[] split_Time(String time) {
		String[] split_Time;
		time = time.trim();
		split_Time = time.split(" ");
		time = split_Time[split_Time.length - 1];
		split_Time = time.split("T");
		time = split_Time[split_Time.length - 1];
		time = time.replace("/", ":");
		time = time.replace("-", ":");
		time = time.replace("：", ":");
		return time.split(":");
	}

	private LocalDateTime to_LocalDateTime(String date) {
			Integer[] split_Date = split_Date(date);
			int year = split_Date[0];
			int month = split_Date[1];
			int day = split_Date[2];
		return to_LocalDateTime(year, month, day);
	}

	private LocalDate to_LocalDate(String date) {
			Integer[] split_Date = split_Date(date);
			int year = split_Date[0];
			int month = split_Date[1];
			int day = split_Date[2];
		return to_LocalDate(year, month, day);
	}

	private boolean is_Int(String string) {
		boolean is_Int = true;
		try {
			__consoleOut__("String = " + Integer.parseInt(string) + " は Integer に変換出来ます");
		} catch (Exception e) {
			__consoleOut__("string = " + string + " は Integer に変換出来ません");
			is_Int = false;
		}
		return is_Int;
	}

	private LocalDateTime to_LocalDateTime(int year, int month, int day) {
		 LocalDate localDate = LocalDate.of(year, month, day);
		LocalDateTime localDateTime = LocalDateTime.now().with(localDate);
		return localDateTime;
	}

	private LocalDate to_LocalDate(int year, int month, int day) {
		LocalDate localDate = LocalDate.of(year, month, day);
		__consoleOut__("localDate = " + localDate);
		return localDate;
	}

	public List<Plan> plan_List(int user_id, LocalDate localDate) {
		return planRepository.plan_List(user_id, localDate);
	}


	public List<Plan> plan_List(int user_id, String year_month) {
		return planRepository.plan_List(user_id, to_LocalDate(year_month));
	}

	/** Double に変換出来るかチエックする */
	private boolean is_Double(String string) {
		boolean is_Double = true;
		try {
			__consoleOut__("String = " + Double.parseDouble(string) + " は Double に変換出来ます");
		} catch (Exception e) {
			__consoleOut__("string = " + string + " は Double に変換出来ません");
			is_Double = false;
		}
		return is_Double;
	}

	public OptionData options() {
		return new OptionData();
	}

	public String this_Year_Month() {
		// 今日の日付を取得
		LocalDateTime now = LocalDateTime.now();
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM");
		return dateTimeFormatter.format(now);
	}

	public String this_Month() {
		// 今日の日付を取得
		LocalDateTime now = LocalDateTime.now();
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM");
		return dateTimeFormatter.format(now);
	}

	public String last_Year_Month() {
		// 今日の日付を取得
			LocalDateTime now = LocalDateTime.now();
		// 1つ前の月に変換
		LocalDateTime last_Month_Now = now.minusMonths(1);
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM");
		return dateTimeFormatter.format(last_Month_Now);
	}

	private LocalDateTime localDateTime(String datetime_Str) {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		return LocalDateTime.parse(datetime_Str, formatter);
	}

	public String now() {
		// 現在日時を取得
		LocalDateTime now = LocalDateTime.now();
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
		return dateTimeFormatter.format(now);
	}

	public String today() {
		// 今日の日付を取得
		LocalDate now = LocalDate.now();
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		return dateTimeFormatter.format(now);
	}

	/** 現在時刻 */
	public String time() {
		// 現在時刻を取得
		LocalDateTime now = LocalDateTime.now();
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		return dateTimeFormatter.format(now);
	}

	/** 現在時刻（5分刻み） */
	public String part_time() {
		String time = time();
		// 末尾を 0 または 5 に変換して time に代入
		// time の1の位を取得
		String minutesLast = time.substring(time.length()-1, time.length());
		// 0 にするか 5 にするか判定
		if (Integer.parseInt(minutesLast) < 5) {
			minutesLast = "0";
		} else {
			minutesLast = "5";
		}
		// 末尾を変換
		time = time.substring(0, time.length()-1) + minutesLast;
		__consoleOut__("part_time = " + time);
		return time;
	}

	public String[] dateOptions(int count, String input) {
		String[] options = null;
		switch(count) {
			case 1:
				options = OptionData.years(today(), 1900, -1);
				break;
			case 2:
				options = OptionData.month();
				break;
			case 3:
				options = OptionData.days(input);
				break;
		}
		return options;
	}

	public String dateInputUrl(int count, int completion_count, String normal_Url, String completion_Url) {
		String dateInputUrl = normal_Url;
		if (count == completion_count) dateInputUrl = completion_Url;
		return dateInputUrl;
	}

	public String date_Input_Stage(int count) {
		String stage = null;
		switch(count) {
		case 1:
			stage = "年";
			break;
		case 2:
			stage = "月";
			break;
		case 3:
			stage = "日";
			break;
		}
		return stage;
	}

	public String dateInputLabel(int count, String label_head) {
		String stage = date_Input_Stage(count);
		return label_head + stage + "を選択して下さい";
	}

	/** 年、月、日の順番で受け取ったそれぞれの数字の桁を合わせ/で繋げる */
	public String dateStringConnect(int count, String string, String input) {
		String delimiter = "/";
		int word_count = 2;
		switch(count) {
			case 1:
				delimiter = "";
				word_count = 0;
				break;
			case 2:
				delimiter = "";
				word_count = 4;
				break;
		}
		// 文字数が word_count になる様に input の前を "0" で埋める
		input = stringAlignment(input, word_count, "0");
		// delimiter で連結し返す
		return string + delimiter + input;
	}

	/** 文字数が word_count になる様に object の前を fill で埋める */
	public static String stringAlignment(Object object, int word_count , String fill) {
		// fill を word_count の数だけ繋げる
		String fills = "";
		for (int i = 0; i < word_count; i++) {
			fills += fill;
		}
		// object の先頭に fills を付け 文字列変数 alignmentString に代入
		String alignmentString = (fills + object);
		//alignmentString の末尾から word_count の数だけ文字を抜き出し 自身に代入
		alignmentString = alignmentString.substring(alignmentString.length() - word_count);
		return alignmentString;
	}

	/** 配列の中身を同じ文字数に揃える */
	public static String[] arrayAlignment(Object[] array, int word_count , String fill) {
		// fill を word_count の数だけ繋げる
		String fills = "";
		for (int i = 0; i < word_count; i++) {
			fills += fill;
		}
		// Object[] array と同じ length の String 配列 alignmentArray を作成
		String[] alignmentArray = new String[array.length];
		for (int i = 0; i < alignmentArray.length; i++) {
			// 先頭に fills を付ける
			String string = (fills + array[i]);
			//末尾から word_count の数だけ文字を抜き出し alignmentArray[i] に代入
			alignmentArray[i] = string.substring(string.length() - word_count);
		}
		return alignmentArray;
	}

	public void birthday_Update(int id, String string, String input) {
		// 文字数が 2 になる様に input の前を "0" で埋める
		input = stringAlignment(input, 2, "0");
		String birthday = string + "/" + input;
		__consoleOut__("birthday = " + birthday);
		// birthday を書き換え
		User user = userRepository.getReferenceById(id);
		user.setBirthday(to_LocalDate(birthday));

		// save() 実行
			userRepository.save(user);
	}

	public void move_in_Update(int id, String string, String input) {
		// 文字数が 2 になる様に input の前を "0" で埋める
		input = stringAlignment(input, 2, "0");
		String move_in = string + "/" + input;
		__consoleOut__("move_in = " + move_in);
		// move_in を書き換え
		User user = userRepository.getReferenceById(id);
		user.setMove_in(to_LocalDate(move_in));
		// save() 実行
		userRepository.save(user);
	}

	public String[] user_Names() {
		return userRepository.names();
	}

	public List<Integer> blankRooms() {
		List<Integer> blankRooms = new ArrayList<>();
		List<User> userList = userRepository.user_List();
		Integer[] roomNumbers = OptionData.room;
		for (Integer roomNumber : roomNumbers) {
			boolean isUse = false;
			for (User user : userList) {
				if(roomNumber == user.getRoom()) isUse = true;
			}
			if (isUse == false) blankRooms.add(roomNumber);
		}
		return blankRooms;
	}

	public int next_User_Id() {
		int nextId = 1;
		User lastElement = getLastElement(userRepository.findAll());
		if (lastElement != null) nextId = lastElement.getId() + 1;
		__consoleOut__("next_User_Id = " + nextId);
		return nextId;
	}

	public int next_Office_Id() {
		int nextId = 1;
		Office lastElement = getLastElement(officeRepository.findAll());
		if (lastElement != null) nextId = lastElement.getId() + 1;
		__consoleOut__("next_Office_Id = " + nextId);
		return nextId;
	}

	public int next_Plan_Id() {
		int nextId = 1;
		List<Plan> plan_List = planRepository.desc_id_All();
		__consoleOut__("plan_List = " + plan_List);
		if (plan_List.size() > 0) nextId = plan_List.get(0).getId() + 1;
		__consoleOut__("next_Plan_Id = " + nextId);
		return nextId;
	}

	public User new_User() {
		return new User(next_User_Id(),0, "", null, "", null, "", "") ;
	}

	public Office new_Office() {
		Office new_Office = new Office(next_Office_Id(),"","" );
		if (new_Office.getId() == 1) set_Office();
		return  new Office(next_Office_Id(),"","" );
	}

	public Plan new_Plan(int user_id) {
		Plan new_Plan = new Plan(0, user_id, null, null,"" ,"" ,"10:00", 0, "", "");
		__consoleOut__("new_Plan = " + new_Plan);
		return new_Plan;
	}


	private void set_Office() {
		String[] item_Names = office_Item_Names();
		officeRepository.save(new Office(1, item_Names[0], ""));
		officeRepository.save(new Office(2, item_Names[1], ""));
	}

	/** List の最後の Element を返すジェネリックメソッド */
	public <E> E getLastElement(List<E> list){
		__consoleOut__("public <E> E getLastElement(List<E> list)開始");
		E lastElement = null;
		if (list.size() > 0) {
			int lastIdx = list.size() - 1;
			lastElement = list.get(lastIdx);
		}
		__consoleOut__("public <E> E getLastElement(List<E> list)終了" + lastElement);
		return lastElement;
	}

	public Object date(String date) {
		if (date == null) date = today();
		return date;
	}


	public String slash_Date(LocalDate localDate) {
		if (localDate == null) localDate = LocalDate.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		return dateTimeFormatter.format(localDate);
	}

	public void __consoleOut__(String message) {
		System.out.println("");
		System.out.println(message);
		System.out.println("");
	}

	private String with_Now(String head_String) {
		String now = now().replaceAll("[^0-9]", ""); // 現在日時の数字以外を "" に変換
//	String now = now().replaceAll("[^\\d]", "");  ←こちらでもOK
		now = now.substring(0, now.length()-3); // 後ろから3文字を取り除く
		return head_String + now;
	}

	public int year(String date) {
		String year = date.trim().split("/")[0].split("-")[0].split("年")[0];
		__consoleOut__(year);
		return Integer.parseInt(year);
	}

	public String month_day(String date) {
		__consoleOut__(date);
		Integer[] split_Date = split_Date(date);
		String month_day = split_Date[1] + "/" + split_Date[2];
		__consoleOut__(month_day);
		return month_day;
	}

	public String month_day_JP(String date) {
		Integer[] split_Date = split_Date(date);
		int split_length = split_Date.length;
		String month_day_JP = split_Date[split_length - 2] + "月" + split_Date[split_length - 1] + "日";
		__consoleOut__(month_day_JP);
		return month_day_JP;
	}

	public String[] plan_Labels() {
		return Set.get_Name_Set(LabelSet.plan_Set);
	}

	public String[] plan_Insert_Labels() {
		return Set.get_Name_Set(LabelSet.plan_Insert_Set);
	}

	public String[] plan_Update_Labels() {
		return Set.get_Name_Set(LabelSet.plan_Update_Set);
	}

	/** コンソールに『クラス名.メソッド名 開始』と出力 */
  public void method_Start() {
  	Class<?> callerClass = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
    .getCallerClass();
  	Method enclosingMethod = callerClass.getEnclosingMethod();
  	System.out.println("");
  	System.out.println(callerClass.getName() + "." + enclosingMethod.getName() + " 開始");
  	System.out.println("");
  }

	/** コンソールに『クラス名.メソッド名 終了』と出力 */
  public void method_End() {
  	Class<?> callerClass = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
  			.getCallerClass();
  	Method enclosingMethod = callerClass.getEnclosingMethod();
  	System.out.println("");
  	System.out.println(callerClass.getName() + "." + enclosingMethod.getName() + " 終了");
  	System.out.println("");
  }

	public String plan_Delete(int id) {
		__consoleOut__("plan_Delete(int id)開始");
		String message = "ID = " + id + " のデータ";
		try {
			planRepository.deleteById(id);
			message += "を削除しました";
		} catch (Exception e) {
			message += "削除に失敗しました " + e.getMessage();
		}
		__consoleOut__("event_Delete(int id)終了");
		return message;
	}

	public String user_Namexxx(int user_id) {
		User user = user(user_id);

		return user.getName() + " " + user.getRoom();
	}

	public String plan_Copy_LastMonth(int user_id, String year_month) {
		__consoleOut__("plan_Copy_LastMonth(int user_id, String year_month)開始");
		Integer[] split_Date = split_Date(year_month);
		String message = split_Date[0] + "/" + (split_Date[1] - 1) + " のデータ複製が";
		try {
			LocalDate lastMonth = to_LocalDate(year_month).minusMonths(1);
			List<Plan> LastMonth_List = planRepository.get_plan_List(user_id, lastMonth);
			for (Plan plan : LastMonth_List) {
				plan = new Plan(plan);
				Integer next_id = next_Plan_Id();
				__consoleOut__("next_id = " + next_id);
				plan.setId(next_id);
				plan.setStart_date(to_LocalDate(year_month));
				planRepository.save(plan);
			}
			message += "完了しました";
		} catch (Exception e) {
			message += "正常に行われませんでした... " + e.getMessage();
		}
		__consoleOut__("plan_Copy_LastMonth(int user_id, String year_month)終了");
		return message;
	}

	public String plan_Copy(int id) {
		__consoleOut__("plan_Copy(int user_id, String year_month)開始");
		String message = "id = " + id + " のデータを複製";
		Plan plan = null;
		try {
			plan = planRepository.get_Plan(id).get(0);
			plan = new Plan(plan);
			Integer next_id = next_Plan_Id();
			__consoleOut__("next_id = " + next_id);
			plan.setId(next_id);
			__consoleOut__("plan = " + plan);
			planRepository.save(plan);
			message += "しました";
		} catch (Exception e) {
			message += "出来ませんでした... " + e.getMessage();
		}
		__consoleOut__("plan_Copy(int user_id, String year_month)終了");
		return message;
	}

}
