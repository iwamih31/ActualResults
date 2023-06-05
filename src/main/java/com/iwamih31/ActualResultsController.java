package com.iwamih31;


import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/ActualResults")
public class ActualResultsController {

	@Autowired
	private ActualResultsService service;

	public String req() {
		return "/ActualResults";
	}

	@GetMapping("/Office")
	public String office(
			@Param("date")String date,
			Model model) {
		service.__consoleOut__("@GetMapping(\"/Office\")開始");
		add_View_Data_(model, "office", "事業所情報");
		String[] item_Names = service.office_Item_Names();
		model.addAttribute("name", item_Names[0]);
		model.addAttribute("department", item_Names[1]);
		model.addAttribute("office_data", service.office_All());
		service.__consoleOut__("@GetMapping(\"/Office\")終了");
		return "view";
	}

	@GetMapping("/OfficeSetting")
	public String officeSetting(
			Model model) {
		service.__consoleOut__("@GetMapping(\"/OfficeSetting\")開始");
		add_View_Data_(model, "officeSetting", "事業所設定");
		model.addAttribute("officeList", service.office_All());
		service.__consoleOut__("@GetMapping(\"/OfficeSetting\")終了");
		return "view";
	}

	@GetMapping("/OfficeInsert")
	public String officeInsert(
			Model model) {
		service.__consoleOut__("@GetMapping(\"/OfficeInsert\")開始");
		add_View_Data_(model, "officeInsert", "新規項目追加");
		model.addAttribute("office", service.new_Office());
		model.addAttribute("next_id", service.next_Office_Id());
		service.__consoleOut__("@GetMapping(\"/OfficeInsert\")終了");
		return "view";
	}

	@PostMapping("/Office/Insert")
	public String office_Insert(
			@RequestParam("post_id")int id,
			@ModelAttribute("office")Office office,
			RedirectAttributes redirectAttributes) {
		service.__consoleOut__("@PostMapping(\"/Office/Insert\")開始");
		String message = service.office_Insert(office, id);
		redirectAttributes.addFlashAttribute("message", message);
		service.__consoleOut__("@PostMapping(\"/Office/Insert\")終了");
		return "redirect:" + req() + "/OfficeSetting";
	}

	@PostMapping("/OfficeUpdate")
	public String officeUpdate(
			@RequestParam("id")int id,
			Model model) {
		service.__consoleOut__("@PostMapping(\"/UserUpdate\")開始");
		add_View_Data_(model, "officeUpdate", "事業所情報更新");
		model.addAttribute("id", id);
		model.addAttribute("office", service.office(id));
		service.__consoleOut__("@PostMapping(\"/UserUpdate\")終了");
		return "view";
	}

	@PostMapping("/Office/Update")
	public String office_Update(
			@RequestParam("post_id")int id,
			@ModelAttribute("office")Office office,
			RedirectAttributes redirectAttributes) {
		service.__consoleOut__("@PostMapping(\"/Office/Update\")開始");
		String message = service.office_Update(office, id);
		redirectAttributes.addFlashAttribute("message", message);
		service.__consoleOut__("@PostMapping(\"/Office/Update\")終了");
		return "redirect:" + req() + "/OfficeSetting";
	}


	@PostMapping("/OfficeReport")
	public String officeReport() {
		service.__consoleOut__("@PostMapping(\"/OfficeReport\")開始");
		service.__consoleOut__("@PostMapping(\"/OfficeReport\")終了");
		return "redirect:/CareRecord/RoutineReport";
	}

	@GetMapping("/OfficeReport")
	public String officeReport(
			Model model) {
		service.__consoleOut__("@GetMapping(\"/OfficeReport\")開始");
		add_View_Data_(model, "officeReport", "事業所情報印刷");
		model.addAttribute("label_Set", LabelSet.officeReport_Set);
		model.addAttribute("office_Report", service.office_Report());
		service.__consoleOut__("@GetMapping(\"/OfficeReport\")終了");
		return "view";
	}

	@PostMapping("/Office/Output/Excel")
	public String office_Output_Excel(
			HttpServletResponse httpServletResponse,
			RedirectAttributes redirectAttributes) {
		service.__consoleOut__("@PostMapping(\"/Office/Output/Excel\")開始");
		String message = service.office_Output_Excel(httpServletResponse);
		redirectAttributes.addFlashAttribute("message", message);
		service.__consoleOut__("@PostMapping(\"/Office/Output/Excel\")終了");
		return "redirect:" + req() + "/CareRecord/OfficeReport";
	}

	@PostMapping("/UserList/Output/Excel")
	public String userList_Output_Excel(
			HttpServletResponse httpServletResponse,
			RedirectAttributes redirectAttributes) {
		service.__consoleOut__("@PostMapping(\"/UserList/Output/Excel\")開始");
		String message = service.userList_Output_Excel(httpServletResponse);
		redirectAttributes.addFlashAttribute("message", message);
		service.__consoleOut__("@PostMapping(\"/UserList/Output/Excel\")終了");
		return "redirect:" + req() + "/CareRecord/OfficeReport";
	}

	@PostMapping("/User/Output/Excel")
	public String user_Output_Excel(
			@RequestParam("user_id")int user_id,
			@RequestParam("year_month")String year_month,
			HttpServletResponse httpServletResponse,
			RedirectAttributes redirectAttributes) {
		service.__consoleOut__("@PostMapping(\"/User/Output/Excel\")開始");
		String message = service.user_Output_Excel(user_id, year_month, httpServletResponse);
		redirectAttributes.addFlashAttribute("message", message);
		service.__consoleOut__("@PostMapping(\"/User/Output/Excel\")終了");
		return "redirect:" + req() + "/CareRecord/OfficeReport";
	}

	@PostMapping("/Plan/Output/Excel")
	public String plan_Output_Excel(
			@RequestParam("user_id")int user_id,
			@RequestParam("year_month")String year_month,
			HttpServletResponse httpServletResponse,
			RedirectAttributes redirectAttributes) {
		service.__consoleOut__("@PostMapping(\"/Plan/Output/Excel\")開始");
		String message = service.plan_Output_Excel(user_id, year_month, httpServletResponse);
		redirectAttributes.addFlashAttribute("message", message);
		service.__consoleOut__("@PostMapping(\"/Plan/Output/Excel\")終了");
		return "redirect:" + req() + "/CareRecord/OfficeReport";
	}

	@PostMapping("/ActualResults/Output/Excel")
	public String actualResults_Output_Excel(
			@RequestParam("year_month")String year_month,
			HttpServletResponse httpServletResponse,
			RedirectAttributes redirectAttributes) {
		service.__consoleOut__("@PostMapping(\"/Plan/Output/Excel\")開始");
		String message = service.actualResults_Output_Excel(year_month, httpServletResponse);
		redirectAttributes.addFlashAttribute("message", message);
		service.__consoleOut__("@PostMapping(\"/Plan/Output/Excel\")終了");
		return "redirect:" + req() + "/CareRecord/OfficeReport";
	}

	@GetMapping("/UserList")
	public String userList(
			Model model) {
		service.__consoleOut__("@GetMapping(\"/UserList\")開始");
		add_View_Data_(model, "userList", "利用者一覧");
		model.addAttribute("year_month", service.this_Year_Month());
		model.addAttribute("userList", service.user_List());
		model.addAttribute("label_Set_List", LabelSet.user_Set);
		service.__consoleOut__("@GetMapping(\"/UserList\")終了");
		return "view";
	}

	@GetMapping("/UserSetting")
	public String userSetting(
			Model model) {
		service.__consoleOut__("@GetMapping(\"/UserSetting\")開始");
		add_View_Data_(model, "userSetting", "利用者設定");
		model.addAttribute("userList", service.user_All());
		model.addAttribute("label_Set_List", LabelSet.user_Set);
		service.__consoleOut__("@GetMapping(\"/UserSetting\")終了");
		return "view";
	}

	@PostMapping("/User")
	public String user(
			@RequestParam("user_id")int user_id,
			@RequestParam("year_month")String year_month) {
		service.__consoleOut__("@PostMapping(\"/User\")開始");
		service.__consoleOut__("@PostMapping(\"/User\")終了");
		return "redirect:" + req() + "/User?id=" + user_id + "&year_month=" + year_month;
	}

	@GetMapping("/User")
	public String user(
			@Param("id")int id,
			@Param("year_month")String year_month,
			Model model) {
		service.__consoleOut__("@GetMapping(\"/User\")開始");
		add_View_Data_(model, "user", "利用者情報");
		model.addAttribute("user_id", id);
		model.addAttribute("year_month", year_month);
		model.addAttribute("user", service.user(id));
		model.addAttribute("label_Set_List", LabelSet.actualResults_Set);
		model.addAttribute("data_Row_Values", service.data_Row_Values_actualResults(id, year_month));
		model.addAttribute("next_URL", req() + "/User");
		service.__consoleOut__("@GetMapping(\"/User\")終了");
		return "view";
	}

	@GetMapping("/UserInsert")
	public String userInsert(
			Model model) {
		service.__consoleOut__("@GetMapping(\"/UserInsert\")開始");
		add_View_Data_(model, "userInsert", "新規利用者登録");
		model.addAttribute("user", service.new_User());
		model.addAttribute("id", service.next_User_Id());
		model.addAttribute("blankRooms", service.blankRooms());
		model.addAttribute("label_Set_List", LabelSet.user_Set);
		model.addAttribute("options", service.options());
		service.__consoleOut__("@GetMapping(\"/UserInsert\")終了");
		return "view";
	}

	@PostMapping("/User/Insert")
	public String user_Insert(
			@RequestParam("post_id")int id,
			@ModelAttribute("user")User user,
			RedirectAttributes redirectAttributes) {
		service.__consoleOut__("@PostMapping(\"/User/Insert\")開始");
		String message = service.user_Insert(user, id);
		redirectAttributes.addFlashAttribute("message", message);
		service.__consoleOut__("@PostMapping(\"/User/Insert\")終了");
		return "redirect:" + req() + "/UserSetting";
	}

	@PostMapping("/UserUpdate")
		public String userUpdate(
				@RequestParam("id")int id
	) {
			service.__consoleOut__("@PostMapping(\"/UserUpdate\")開始");
			service.__consoleOut__("@PostMapping(\"/UserUpdate\")終了");
			return "redirect:" + req() + "/UserUpdate?id=" + id;
		}

	@GetMapping("/UserUpdate")
	public String userUpdate(
			@Param("id")int id,
			Model model) {
		service.__consoleOut__("@GetMapping(\"/UserUpdate\")開始");
		add_View_Data_(model, "userUpdate", "利用者情報更新");
		model.addAttribute("id", id);
		model.addAttribute("user", service.user(id));
		model.addAttribute("blankRooms", service.blankRooms());
		model.addAttribute("label_Set_List", LabelSet.user_Set);
		model.addAttribute("options", service.options());
		service.__consoleOut__("@GetMapping(\"/UserUpdate\")終了");
		return "view";
	}

	@PostMapping("/User/Update")
	public String user_Update(
			@RequestParam("post_id")int id,
			@ModelAttribute("user")User user,
			RedirectAttributes redirectAttributes) {
		service.__consoleOut__("@PostMapping(\"/User/Update\")開始");
		String message = service.user_Update(user, id);
		redirectAttributes.addFlashAttribute("message", message);
		service.__consoleOut__("@PostMapping(\"/User/Update\")終了");
		return "redirect:" + req() + "/UserSetting";
	}

	@PostMapping("/Plan")
	public String plan(
			@RequestParam("user_id")int user_id,
			@RequestParam("year_month")String year_month) {
		service.__consoleOut__("@PostMapping(\"/User\")開始");
		service.__consoleOut__("@PostMapping(\"/User\")終了");
		return "redirect:" + req() + "/Plan?user_id=" + user_id + "&year_month=" + year_month;
	}

	@GetMapping("/Plan")
	public String plan(
			@Param("user_id")int user_id,
			@Param("year_month")String year_month,
			Model model) {
		service.__consoleOut__("@PostMapping(\"/Plan\")開始");
		add_View_Data_(model, "plan", "訪問計画");
		model.addAttribute("user_id", user_id);
		model.addAttribute("year_month", year_month);
		model.addAttribute("user", service.user(user_id));
		model.addAttribute("label_Set_List", LabelSet.plan_Set);
		model.addAttribute("plan_List", service.plan_List(user_id, year_month));
		service.__consoleOut__("@PostMapping(\"/Plan\")終了");
		return "view";
	}

	@PostMapping("/PlanInsert")
	public String planInsert(
			@RequestParam("user_id")int user_id,
			@RequestParam("year_month")String year_month,
			Model model) {
		service.__consoleOut__("@PostMapping(\"/PlanInsert\")開始");
		add_View_Data_(model, "planInsert", "新規訪問予定登録");
		model.addAttribute("user_id", user_id);
		model.addAttribute("year_month", year_month);
		model.addAttribute("user", service.user(user_id));
		model.addAttribute("plan", service.new_Plan(user_id));
		model.addAttribute("label", Set.get_Name_Set(LabelSet.plan_Insert_Set));
		model.addAttribute("options", service.options());
		service.__consoleOut__("@PostMapping(\"/PlanInsert\")終了");
		return "view";
	}

	@PostMapping("/Plan/Insert")
	public String plan_Insert(
			@RequestParam("user_id")int user_id,
			@RequestParam("year_month")String year_month,
			@RequestParam("day_of_weeks")String[] day_of_weeks,
			@ModelAttribute("plan")Plan plan,
			RedirectAttributes redirectAttributes) {
		service.__consoleOut__("@PostMapping(\"/Plan/Insert\")開始");
		String message = service.plan_Insert(plan, user_id, year_month, day_of_weeks);
		redirectAttributes.addFlashAttribute("message", message);
		service.__consoleOut__("@PostMapping(\"/Plan/Insert\")終了");
		return "redirect:" + req() + "/Plan?user_id=" + user_id + "&year_month=" + year_month;
	}

	@PostMapping("/PlanUpdate")
	public String planUpdate(
			@RequestParam("id")int id,
			@RequestParam("user_id")int user_id,
			@RequestParam("year_month")String year_month
			) {
		service.__consoleOut__("@PostMapping(\"/PlanUpdate\")開始");
		if (service.exists_Plan(id) == false ) {
			return "redirect:" + req() + "/Plan?user_id=" + user_id + "&year_month=" + year_month;
		}
		service.__consoleOut__("@PostMapping(\"/PlanUpdate\")終了");
		return "redirect:" + req() + "/PlanUpdate?id=" + id + "&user_id=" + user_id + "&year_month=" + year_month;
	}

	@GetMapping("/PlanUpdate")
	public String planUpdate(
			@Param("id")int id,
			@Param("user_id")int user_id,
			@Param("year_month")String year_month,
			Model model) {
		service.__consoleOut__("@GetMapping(\"/PlanUpdate\")開始 ");
		add_View_Data_(model, "planUpdate", "訪問計画更新");
		model.addAttribute("id", id);
		model.addAttribute("user_id", user_id);
		model.addAttribute("year_month", year_month);
		model.addAttribute("plan", service.plan(id));
		model.addAttribute("label", Set.get_Name_Set(LabelSet.plan_Update_Set));
		model.addAttribute("user_namexxx", service.user_Namexxx(user_id));
		model.addAttribute("options", service.options());
		service.__consoleOut__("@GetMapping(\"/PlanUpdate\")終了");
		return "view";
	}

	@PostMapping("/Plan/Update")
	public String plan_Update(
			@RequestParam("user_id")int user_id,
			@RequestParam("year_month")String year_month,
			@RequestParam("day_of_weeks")String[] day_of_weeks,
			@ModelAttribute("plan")Plan plan,
			RedirectAttributes redirectAttributes) {
		service.__consoleOut__("@PostMapping(\"/Plan/Update\")開始");
		String message = service.plan_Update(plan, day_of_weeks);
		redirectAttributes.addFlashAttribute("message", message);
		service.__consoleOut__("@PostMapping(\"/Plan/Update\")終了");
		return "redirect:" + req() + "/Plan?user_id=" + user_id + "&year_month=" + year_month;
	}

	@PostMapping("/PlanDelete")
	public String planDelete(
			@RequestParam("id")int id,
			@RequestParam("user_id")int user_id,
			@RequestParam("year_month")String year_month
			) {
		service.__consoleOut__("@PostMapping(\"/PlanDelete\")開始");
		service.__consoleOut__("@PostMapping(\"/PlanDelete\")終了");
		return "redirect:" + req() + "/PlanDelete?id=" + id + "&user_id=" + user_id + "&year_month=" + year_month;
	}

	@GetMapping("/PlanDelete")
	public String planDelete(
			@Param("id")int id,
			@Param("user_id")int user_id,
			@Param("year_month")String year_month,
			Model model) {
		service.__consoleOut__("@GetMapping(\"/PlanDelete\")開始 ");
		add_View_Data_(model, "planDelete", "訪問計画削除");
		model.addAttribute("id", id);
		model.addAttribute("user_id", user_id);
		model.addAttribute("year_month", year_month);
		model.addAttribute("label_Set_List", LabelSet.plan_Delete_Set);
		model.addAttribute("user", service.user(user_id));
		model.addAttribute("plan", service.plan(id));
		service.__consoleOut__("@GetMapping(\"/PlanDelete\")終了");
		return "view";
	}

	@PostMapping("/Plan/Delete")
	public String plan_Delete(
			@RequestParam("id")int id,
			@RequestParam("user_id")int user_id,
			@RequestParam("year_month")String year_month,
			RedirectAttributes redirectAttributes) {
		service.__consoleOut__("@PostMapping(\"/Plan/Update\")開始");
		String message = service.plan_Delete(id);
		redirectAttributes.addFlashAttribute("message", message);
		service.__consoleOut__("@PostMapping(\"/Plan/Update\")終了");
		return "redirect:" + req() + "/Plan?user_id=" + user_id + "&year_month=" + year_month;
	}

@GetMapping("/Report")
	public String report(
			Model model) {
		service.__consoleOut__("@GetMapping(\"/Report\")開始");
		add_View_Data_(model, "report", "帳票印刷メニュー");
		service.__consoleOut__("@GetMapping(\"/Report\")終了");
		return "view";
	}

	@GetMapping("/Setting")
	public String setting(
			Model model) {
		service.__consoleOut__("@GetMapping(\"/Setting\")開始");
		add_View_Data_(model, "setting", "設定メニュー");
		service.__consoleOut__("@GetMapping(\"/Setting\")終了");
		return "view";
	}

	@PostMapping("/Plan/Copy")
	public String plan_Copy(
			@RequestParam("id")int id,
			@RequestParam("user_id")int user_id,
			@RequestParam("year_month")String year_month,
			RedirectAttributes redirectAttributes) {
		service.__consoleOut__("@PostMapping(\"/Plan/Copy\")開始");
		String message = service.plan_Copy(id);
		redirectAttributes.addFlashAttribute("message", message);
		service.__consoleOut__("@PostMapping(\"/Plan/Copy\")終了");
		return "redirect:" + req() + "/Plan?user_id=" + user_id + "&year_month=" + year_month;
	}

	@PostMapping("/Plan/Copy/LastMonth")
	public String plan_Copy_LastMonth(
			@RequestParam("user_id")int user_id,
			@RequestParam("year_month")String year_month,
			RedirectAttributes redirectAttributes) {
		service.__consoleOut__("@PostMapping(\"/Plan/Copy/LastMonth\")開始");
		String message = service.plan_Copy_LastMonth(user_id, year_month);
		redirectAttributes.addFlashAttribute("message", message);
		service.__consoleOut__("@PostMapping(\"/Plan/Copy/LastMonth\")終了");
		return "redirect:" + req() + "/Plan?user_id=" + user_id + "&year_month=" + year_month;
	}

	@PostMapping("/Plan/YearMonth")
	public String plan_YearMonth(
			@RequestParam("user_id")int user_id,
			@RequestParam("year_month")String year_month,
			Model model) {
		service.__consoleOut__("@PostMapping(\"/Plan/YearMonth\")開始 ");
		add_View_Data_(model, "yearMonth", "日付変更");
		model.addAttribute("user_id", user_id);
		model.addAttribute("year_month", year_month);
		model.addAttribute("url", "/Plan");
		model.addAttribute("user", service.user(user_id));
		model.addAttribute("year", service.year(year_month));
		model.addAttribute("month_day", service.month_day(year_month));
		model.addAttribute("options", service.options());
		service.__consoleOut__("@PostMapping(\"/Plan/YearMonth\")終了");
		return "view";
	}

	@PostMapping("/User/YearMonth")
	public String user_YearMonth(
			@RequestParam("user_id")int user_id,
			@RequestParam("year_month")String year_month,
			Model model) {
		service.__consoleOut__("@PostMapping(\"/User/YearMonth\")開始 ");
		add_View_Data_(model, "yearMonth", "日付変更");
		model.addAttribute("user_id", user_id);
		model.addAttribute("year_month", year_month);
		model.addAttribute("url", "/User");
		model.addAttribute("user", service.user(user_id));
		model.addAttribute("options", service.options());
		service.__consoleOut__("@PostMapping(\"/User/YearMonth\")終了");
		return "view";
	}

	@GetMapping("/ActualResults")
	public String actualResults(
			Model model) {
		service.__consoleOut__("@GetMapping(\"/ActualResults\")開始 ");
		add_View_Data_(model, "actualResults", "実施記録票（全利用者分）出力");
		model.addAttribute("year_month", service.this_Year_Month());
		model.addAttribute("options", service.options());
		service.__consoleOut__("@GetMapping(\"/ActualResults\")終了");
		return "view";
	}

	@PostMapping("/Birthday")
	public String birthday(
			@RequestParam("id")int id,
			@RequestParam("count")int count,
			@RequestParam("string")String string,
			@RequestParam("input")String input) {
		service.__consoleOut__("@PostMapping(\"/Birthday\")開始");
		service.__consoleOut__("@PostMapping(\"/Birthday\")終了");
		return "redirect:" + req() + "/Birthday?id=" + id + "&count=" + count + "&string=" + string + "&input=" + input;
	}

	@GetMapping("/Birthday")
	public String birthday(
			@Param("id")int id,
			@Param("count")int count,
			@Param("string")String string,
			@Param("input")String input,
			Model model) {
		service.__consoleOut__("@GetMapping(\"/Birthday\")開始");
		service.__consoleOut__("string = " + service.dateStringConnect(count, string, input));
		add_View_Data_(model, "birthday", "誕生日登録");
		model.addAttribute("user", service.user(id));
		model.addAttribute("count", count + 1);
		model.addAttribute("label", service.dateInputLabel(count, "生まれた"));
		model.addAttribute("string", service.dateStringConnect(count, string, input));
		model.addAttribute("options", service.dateOptions(count, input));
		String url = service.dateInputUrl(count, 3, "" + req() + "/Birthday", "" + req() + "/Birthday/Update");
		model.addAttribute("url", url);
		service.__consoleOut__(url);
		service.__consoleOut__("@GetMapping(\"/Birthday\")" + count + "終了");
		return "view";
	}

	@PostMapping("/Birthday/Update")
	public String birthday_Update(
			@RequestParam("id")int id,
			@RequestParam("count")int count,
			@RequestParam("string")String string,
			@RequestParam("input")String input) {
		service.__consoleOut__("@PostMapping(\"/Birthday/Update\")開始");
		service.__consoleOut__("count = " + count);
		service.birthday_Update(id, string, input);
		service.__consoleOut__("@PostMapping(\"/Birthday/Update\")終了");
		return "redirect:" + req() + "/UserUpdate?id=" + id;
	}

	@PostMapping("/Move_in")
	public String move_in(
			@RequestParam("id")int id,
			@RequestParam("count")int count,
			@RequestParam("string")String string,
			@RequestParam("input")String input) {
		service.__consoleOut__("@PostMapping(\"/Move_in\")開始");
		service.__consoleOut__("@PostMapping(\"/Move_in\")終了");
		return "redirect:" + req() + "/Move_in?id=" + id + "&count=" + count + "&string=" + string + "&input=" + input;
	}

	@GetMapping("/Move_in")
	public String move_in(
			@Param("id")int id,
			@Param("count")int count,
			@Param("string")String string,
			@Param("input")String input,
			Model model) {
		service.__consoleOut__("@GetMapping(\"/Move_in\")開始");
		service.__consoleOut__("string = " + service.dateStringConnect(count, string, input));
		add_View_Data_(model, "input", "入居" + service.date_Input_Stage(count));
		model.addAttribute("id", id);
		model.addAttribute("count", count + 1);
		model.addAttribute("label", service.dateInputLabel(count, "入居した"));
		model.addAttribute("string", service.dateStringConnect(count, string, input));
		model.addAttribute("options", service.dateOptions(count, input));
		String url = service.dateInputUrl(count, 3, "" + req() + "/Move_in", "" + req() + "/Move_in/Update");
		model.addAttribute("url", url);
		service.__consoleOut__(url);
		service.__consoleOut__("@GetMapping(\"/Move_in\")" + count + "終了");
		return "view";
	}

	@PostMapping("/Move_in/Update")
	public String move_in_Update(
			@RequestParam("id")int id,
			@RequestParam("count")int count,
			@RequestParam("string")String string,
			@RequestParam("input")String input) {
		service.__consoleOut__("@PostMapping(\"/Move_in/Update\")開始");
		service.__consoleOut__("count = " + count);
		service.move_in_Update(id, string, input);
		service.__consoleOut__("@PostMapping(\"/Move_in/Update\")終了");
		return "redirect:" + req() + "/UserUpdate?id=" + id;
	}

	/** view 表示に必要な属性データをモデルに登録 */
	private void add_View_Data_(Model model, String template, String title) {
		model.addAttribute("library", template + "::library");
		model.addAttribute("main", template + "::main");
		model.addAttribute("title", title);
		model.addAttribute("req", "/ActualResults");
		System.out.println("template = " + template);
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


}
