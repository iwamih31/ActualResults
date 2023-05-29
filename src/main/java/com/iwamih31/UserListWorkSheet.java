package com.iwamih31;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.IndexedColors;

public class UserListWorkSheet extends WorkSheet{

	public UserListWorkSheet() {
		this.printSetup = true;
	}
	// コンストラクター
	public UserListWorkSheet(String sheet_Name, int[] column_Width, String[][] value_Data) {
		this.sheet_Name = sheet_Name;
		this.column_Width = column_Width;
		this.value_Data = value_Data;
		// 印刷向きを横にする
		this.printSetup = true;
	}

	// 罫線（"□","￣","＿"," |","| ","二","冂","凵","匚","コ","ノ","乚","ｒ","¬"）
	// 位置（"｜","CS","DD","FL","JY","←","→"）
	// １行目
	String[] row_1_Border = {"  ","  ","  ","  ","  ","  ","  ","  "}; // 罫線
	String[] row_1_Align_ = {"  ","  ","  ","  ","  ","  ","  ","  "}; // 位置
	// ２行目
	String[] row_2_Border = {"匚","二","二","二","二","二","二","コ"}; // 罫線
	String[] row_2_Align_ = {"  ","  ","  ","  ","  ","  ","  ","  "}; // 位置
	// ３行目
	String[] row_3_Border = {"  ","  ","  ","  ","  ","  ","  ","  "}; // 罫線
	String[] row_3_Align_ = {"  ","  ","  ","  ","  ","  ","  ","  "}; // 位置
	// ４行目（ラベル行）
	String[] label_Border = {"□","□","□","□","□","□","□","□"}; // 罫線
	String[] label_Align_ = {"｜","｜","｜","｜","｜","｜","｜","｜"}; // 位置
	// その他（データ行）
	String[] data__Border = {"□","□","□","□","□","□","□","□"}; // 罫線
	String[] data__Align_ = {"｜","｜","｜","｜","｜","｜","｜","｜"}; // 位置

	/** フォント定義用 Map リスト */
	public List<Map<String, String>> fonts(){
		List<Map<String, String>> fonts = new ArrayList<>();
		Map<String, String> font0 = new HashMap<>();
		Map<String, String> font1 = new HashMap<>();
		// フォント0
		font0.put("fontName", "ＭＳ Ｐゴシック");
		font0.put("fontHeight", "150");
		// フォント1
		font1.put("fontName", "ＭＳ Ｐゴシック");
		font1.put("fontHeight", "200");
		fonts.add(font0);
		fonts.add(font1);
		return fonts;
	};

	/** 行毎の書式 */
	public List<Map<String, String[]>> row_Format(int row_Size){
		List<Map<String, String[]>> row_Format = new ArrayList<>();
		for (int i = 0; i < row_Size; i++) {
			// default の値
			int height = 400;
			int font = 0;
			short bg_color = IndexedColors.AUTOMATIC.getIndex();
			String[] border;
			String[] align;
			switch (i + 1) {
				case 1: // １行目の場合
					font = 1;
					height = 500;
					border = row_1_Border;
					align = row_1_Align_;
					break;
				case 2: // ２行目の場合
					font = 1;
					height = 500;
					border = row_2_Border;
					align = row_2_Align_;
					break;
				case 3: // ３行目の場合
					height = 100;
					border = row_3_Border;
					align = row_3_Align_;
					break;
				case 4: // ４行目（ラベル行）の場合
					bg_color = IndexedColors.GREY_25_PERCENT.getIndex();
					border = label_Border;
					align = label_Align_;
					break;
				default: // その他（データ行）の場合
					border = data__Border;
					align = data__Align_;
			}
			Map<String, String[]> row_Map = new HashMap<>();
			row_Map.put("border", border);
			row_Map.put("align", align);
			row_Map.put("height", array(height));
			row_Map.put("font", array(font));
			row_Map.put("bg_color", array(bg_color));
			row_Format.add(row_Map);
		}
		return row_Format;
	}

	private String[] array(String value) {
		return new String[] {value};
	}

	private String[] array(int value) {
		return new String[] {String.valueOf(value)};
	}

	public String getSheet_Name() {
		return sheet_Name;
	}


	public void setSheet_Name(String sheet_Name) {
		this.sheet_Name = sheet_Name;
	}


	public int[] getColumn_Width() {
		return column_Width;
	}


	public void setColumn_Width(int[] column_Width) {
		this.column_Width = column_Width;
	}
}
