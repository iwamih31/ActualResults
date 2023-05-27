package com.iwamih31;

public class LabelSet {

	public static Set[] userReport_Set = {
			set("ID",				3),
			set("部屋番号",	5),
			set("氏名",		 10),
			set("誕生日",		8),
			set("介護度",		5),
			set("入居日",		8),
			set("利用状況",	8)
		};

	public static Set[] routineReport_Set = {
			set("日付",			6),
			set("部屋番号",	4),
			set("氏名",			8),
			set("時間",			5),
			set("予定",			8),
			set("内容",		 20),
			set("ID",				3)
		};

	public static Set[] officeReport_Set = {
			set("ID",				6),
			set("項目",			4),
			set("内容",			8)
		};

	public static Set[] actualResults_Set = {
			set("日付",								3),
			set("曜日",								2),
			set("サービス　内容　",		3),
			set("サービス提供時間",		5),
			set("サービス内容",			 12),
			set("記録",							 9),
			set("利用者　印",					3),
			set("ヘルパー　氏名　",		3)
		};

	public static Set[] plan_Set = {
			set("サービス",			5),
			set("曜日",					9),
			set("開始時刻",			5),
			set("実施分数",			5),
			set("サービス内訳",	16),
			set("備考",					16)
		};

	public static Set[] plan_Insert_Set = {
			set("訪問予定曜日",	4),
			set("サービス内容",	4),
			set("訪問開始時刻",	4),
			set("サービス内訳",	4),
			set("備　　　　考",	8)
		};

	public static Set[] plan_Update_Set = {
			set("I　　D",	 4),
			set("利用者",	10),
			set("開始日",	 6),
			set("終了日",	 6),
			set("曜　日",	 4),
			set("内　容",	 5),
			set("時　刻",	 4),
			set("分　数",	 4),
			set("内　訳",	10),
			set("備　考",	10)
		};

	public static Set[] plan_Delete_Set = {
			set("ID",			 3),
			set("利用者",	 8),
			set("対象月",	 6),
			set("曜日",		 8),
			set("内容",		 4),
			set("時刻",		 4),
			set("分数",		 3),
			set("内訳",		 9),
			set("備考",		 9)
		};


	public static Set set(String label_Name, int width) {
		return new Set(label_Name, width);
	}
}
