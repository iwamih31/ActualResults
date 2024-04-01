package com.iwamih31;

import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.AttributedString;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {

	/** alignment_Pattern に応じてセルの alignment を設定 */
	public CellStyle alignment_Apply(CellStyle cellStyle, String alignment_Pattern) {
		if(alignment_Pattern != null) {
			switch(alignment_Pattern) {
				case "｜":
					cellStyle.setAlignment(HorizontalAlignment.CENTER);
						break;
				case "CS":
					cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
						break;
				case "DD":
					cellStyle.setAlignment(HorizontalAlignment.DISTRIBUTED);
						break;
				case "FL":
					cellStyle.setAlignment(HorizontalAlignment.FILL);
						break;
				case "JY":
					cellStyle.setAlignment(HorizontalAlignment.JUSTIFY);
						break;
				case "←":
					cellStyle.setAlignment(HorizontalAlignment.LEFT);
						break;
				case "→":
					cellStyle.setAlignment(HorizontalAlignment.RIGHT);
						break;
				default:
					cellStyle.setAlignment(HorizontalAlignment.GENERAL);
			}
		}
		return cellStyle;
	}

	/** border_Pattern に応じてセルの 罫線 を設定 */
	public CellStyle border_Apply(CellStyle cellStyle, String border_Pattern) {
		if(border_Pattern != null) {
			switch(border_Pattern) {
			case "□":
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.THIN);
				break;
			case "￣":
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setBorderBottom(BorderStyle.NONE);
				cellStyle.setBorderLeft(BorderStyle.NONE);
				cellStyle.setBorderRight(BorderStyle.NONE);
				break;
			case "＿":
				cellStyle.setBorderTop(BorderStyle.NONE);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.NONE);
				cellStyle.setBorderRight(BorderStyle.NONE);
				break;
			case " |":
				cellStyle.setBorderTop(BorderStyle.NONE);
				cellStyle.setBorderBottom(BorderStyle.NONE);
				cellStyle.setBorderLeft(BorderStyle.NONE);
				cellStyle.setBorderRight(BorderStyle.THIN);
				break;
			case "| ":
				cellStyle.setBorderTop(BorderStyle.NONE);
				cellStyle.setBorderBottom(BorderStyle.NONE);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.NONE);
				break;
			case "二":
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.NONE);
				cellStyle.setBorderRight(BorderStyle.NONE);
				break;
			case "||":
				cellStyle.setBorderTop(BorderStyle.NONE);
				cellStyle.setBorderBottom(BorderStyle.NONE);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.THIN);
				break;
			case "冂":
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setBorderBottom(BorderStyle.NONE);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.THIN);
				break;
			case "凵":
				cellStyle.setBorderTop(BorderStyle.NONE);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.THIN);
				break;
			case "匚":
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.NONE);
				break;
			case "コ":
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.NONE);
				cellStyle.setBorderRight(BorderStyle.THIN);
				break;
			case "ノ":
				cellStyle.setBorderTop(BorderStyle.NONE);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.NONE);
				cellStyle.setBorderRight(BorderStyle.THIN);
				break;
			case "乚":
				cellStyle.setBorderTop(BorderStyle.NONE);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.NONE);
				break;
			case "ｒ":
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setBorderBottom(BorderStyle.NONE);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.NONE);
				break;
			case "¬":
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setBorderBottom(BorderStyle.NONE);
				cellStyle.setBorderLeft(BorderStyle.NONE);
				cellStyle.setBorderRight(BorderStyle.THIN);
				break;
			case "  ":
				cellStyle.setBorderTop(BorderStyle.NONE);
				cellStyle.setBorderBottom(BorderStyle.NONE);
				cellStyle.setBorderLeft(BorderStyle.NONE);
				cellStyle.setBorderRight(BorderStyle.NONE);
				break;
			}
		}
		return cellStyle;
	}

	// 色指定
	private void bg_color_Apply(CellStyle cellStyle, String color_Index) {
		if(color_Index != null) {
			short bg_Index = (short) Integer.parseInt(color_Index);
			// color_Index が AUTOMATIC のindexだったら何もしない
			if(bg_Index != IndexedColors.AUTOMATIC.getIndex()) {
				cellStyle.setFillForegroundColor((short) Integer.parseInt(color_Index));
				cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			}
		}
	}

	/** 1シート分のデータを 1つのExcelファイル として出力 */
	public String output_Excel_Sheet(String name_Head, WorkSheet workSheet, HttpServletResponse response) {
		___console_Out___("output_Excel_Sheet() 開始");
		String file_Name = with_Now(name_Head) + ".xlsx";
		String message = file_Name + " のダウンロード";
		try (
				Workbook workbook = new XSSFWorkbook();
				OutputStream outputStream = response.getOutputStream()){
			sheet_Making(workbook, workSheet);
			// ファイル名を指定して保存
			if (response_Making(response, file_Name)) {
				workbook.write(outputStream);
				message += "が完了しました";
			}
			workbook.close();
			message += " workbook を close() しました";
		} catch (IOException e) {
			message += "が正常に完了出来ませんでした";
			___console_Out___(e.getMessage());
		}
		___console_Out___("output_Excel() 終了");
		return message;
	}

	/** 複数シート分のデータを 1つのExcelファイル として出力 */
	public String output_Excel_Sheets(String name_Head, List<WorkSheet> workSheets, HttpServletResponse response) {
		___console_Out___("output_Excel() 開始");
		String file_Name = with_Now(name_Head) + ".xlsx";
		String message = file_Name + " のダウンロード";
		try (
				Workbook workbook = new XSSFWorkbook();
	      OutputStream outputStream = response.getOutputStream()){
			// workSheets の要素数回ループ
			for (WorkSheet workSheet : workSheets) {
				// workSheet を元に シートを作成
				sheet_Making(workbook, workSheet);
			}
	    // ファイル名を指定して保存
			if (response_Making(response, file_Name)) {
				workbook.write(outputStream);
				message += "が完了しました";
			}
			workbook.close();
			message += " workbook を close() しました";
		} catch (IOException e) {
			message += "が正常に完了出来ませんでした";
			___console_Out___(e.getMessage());
		}
		___console_Out___("output_Excel() 終了");
		return message;
	}

	/** Sheetを 作成 */
	public Sheet sheet_Making(Workbook workbook, WorkSheet work_Sheet) {
		___console_Out___("sheet_Making() 開始");
		/* シートを作成 */
		// シート名取得
		String sheet_Name = work_Sheet.getSheet_Name();
		___console_Out___("sheet_Name = " + sheet_Name);
		// シート名 sheet_Name のシートを作成
		Sheet sheet = workbook.createSheet(sheet_Name);
		String[][] value_Data = work_Sheet.getValue_Data();
		// 行数取得
		int row_Size = value_Data.length;
		// 書式定義用データのリスト取得
		List<Map<String, String[]>> row_Format = work_Sheet.row_Format(row_Size);
		/* 使用するフォントを定義 */
		// フォント定義用データリスト取得
		List<Map<String, String>> work_Sheet_Fonts = work_Sheet.fonts();
		// フォント格納用リスト作成
		List<Font>fonts = new ArrayList<>();
		// フォント格納用リスト分ループ
		for (Map<String, String> work_Sheet_Font : work_Sheet_Fonts) {
			// 新しいフォント作成
			Font font = workbook.createFont();
			// フォント名取得
			font.setFontName(work_Sheet_Font.get("fontName"));
			// フォントの高さ取得
			font.setFontHeight((short) Integer.parseInt(work_Sheet_Font.get("fontHeight")));
			// フォント格納用リストにフォントをセット
			fonts.add(font);
		}
		// 行ループ
		for (int i = 0; i < row_Size; i++) {
			// 行を定義
			Row row = sheet.createRow(i);
			// 行フォーマット
			Map<String, String[]> row_Map = row_Format.get(i);
			// 行の高さを取得
			int height = Integer.parseInt(row_Map.get("height")[0]);
			// 取得した高さを行にセット
			row.setHeight((short) height);
			// 列数取得
			int column_Size = value_Data[i].length;
			// 列数分ループ
			for (int j = 0; j < column_Size; j++) {
				// セルを定義
				Cell cell = row.createCell(j);
				String value = value_Data[i][j];
				// セルに値をセット
				if(is_Double(value)){
					cell.setCellValue(Double.parseDouble(value));
				} else {
					if (!value.equals("")) cell.setCellValue(value);
				}
				// セルスタイルを定義
				CellStyle cellStyle = workbook.createCellStyle();
				// セルのフォントスタイルのリスト番号を取得
				int font_Num = Integer.parseInt(row_Map.get("font")[0]);
				// 取得した番号からセルのフォント決定
				Font font = fonts.get(font_Num);
				// 決定したフォントを cellStyle にセット
				cellStyle.setFont(font);
				// 背景色指定
				if (row_Map.get("bg_color") != null) {
					bg_color_Apply(cellStyle, row_Map.get("bg_color")[0]);
				}
				// 縦配置
				cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				// 横配置
				alignment_Apply(cellStyle, row_Map.get("align")[j]);
				// 罫線指定
				border_Apply(cellStyle,  row_Map.get("border")[j]);
				// セルにセルスタイルを適用
				cell.setCellStyle(cellStyle);
				// 最後の行のみ
				if (i == row_Size - 1) {
					int[] column_Width = work_Sheet.getColumn_Width();
					if (column_Width != null) {
						// 列幅設定（1文字分の横幅 × 文字数 ＋ 微調整分の幅）
						sheet.setColumnWidth(j, 512 * column_Width[j] + 0);
					}
					// 列幅設定（オート）
					// sheet.autoSizeColumn(j);
					// 最後の列のみ印刷範囲設定
					if(j == column_Size - 1) {
						// 印刷範囲設定
						workbook.setPrintArea(0, 0, j, 0, i);
					}
				}
			}
		}
//		sheet.setColumnBreak(7); // 改ページ位置設定
//		sheet.removeColumnBreak(4);
    PrintSetup printSetup = sheet.getPrintSetup();
    printSetup.setLandscape(work_Sheet.printSetup);
		for (int rowBreak : sheet.getRowBreaks()) {
	    sheet.removeRowBreak(rowBreak);
	}
		for (int colBreak : sheet.getColumnBreaks()) {
	    sheet.removeColumnBreak(colBreak);
	}
		___console_Out___("sheet_Making() 終了");
		return sheet;
	}

	/** 指定列の改ページ解除 */
	@SuppressWarnings("unused")
	private void remove_ColumnBreak(Sheet sheet, int column_Number) {
	if (sheet.isColumnBroken(column_Number))sheet.removeColumnBreak(column_Number);
	}

	/** 指定列の右側に改ページ設定 */
	@SuppressWarnings("unused")
	private void set_ColumnBreak(Sheet sheet, int column_Number) {
		sheet.setColumnBreak(column_Number);
	}

	/** 指定行の改ページ解除 */
	@SuppressWarnings("unused")
	private void remove_RowBreak(Sheet sheet, int row_Number) {
	if (sheet.isRowBroken(row_Number))sheet.removeRowBreak(row_Number);
	}

	/** 指定行の下側に改ページ設定 */
	@SuppressWarnings("unused")
	private void set_RowBreak(Sheet sheet, int row_Number) {
		sheet.setRowBreak(row_Number);
	}

	/** 自動改ページ無効 */
	@SuppressWarnings("unused")
	private void invalid_Autobreaks(Sheet sheet) {
	sheet.setAutobreaks(false);
	}

	/** シート分割（スクロール固定） */
	@SuppressWarnings("unused")
	private Sheet createSplitPane(
			Sheet sheet,
			int xSplitPos,
      int ySplitPos,
      int leftmostColumn,		// 8?
      int topRow,						// 20?
      int activePane 	// Sheet.PANE_LOWER_RIGHT
      								// Sheet.PANE_UPPER_RIGHT
								      // Sheet.PANE_LOWER_LEFT
								      // Sheet.PANE_UPPER_LEFT
      ) {
		sheet.createSplitPane(xSplitPos, ySplitPos, leftmostColumn, topRow, activePane);
		return sheet;
	}

	private boolean response_Making(HttpServletResponse response, String file_Name) {
		boolean is_Make = false;
		String encodedFilename = with_Now("create") + ".xlsx";
		try {
			encodedFilename = URLEncoder.encode(file_Name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		___console_Out___("file_Name を " + encodedFilename + "に設定しました");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	  response.setHeader("Content-Disposition", "attachment;filename=\"" + encodedFilename + "\"");
	  response.setCharacterEncoding("UTF-8");
	  is_Make = true;
		return is_Make;
	}

	/** Table データを Excel として出力 */
	public String output_Excel(String name_Head, String[] column_Names, int[] column_Width, String[][] table_Data, HttpServletResponse response) {
		___console_Out___("output_Excel() 開始");
		String file_Name = with_Now(name_Head) + ".xlsx";
		String sheet_Name = with_Now(name_Head);
		String message = file_Name + " のダウンロード";
		try (Workbook workbook = new XSSFWorkbook();
        	OutputStream outputStream = response.getOutputStream()){
			Sheet sheet = workbook.createSheet(sheet_Name);
			// 使用するフォントを定義
			Font font = workbook.createFont();
			font.setFontName("游ゴシック");
			// ヘッダー行
			// セルスタイルを定義
			CellStyle header_CellStyle = workbook.createCellStyle();
			// 色指定
			header_CellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			header_CellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			// 罫線指定
			header_CellStyle.setBorderTop(BorderStyle.THIN);
			header_CellStyle.setBorderBottom(BorderStyle.THIN);
			header_CellStyle.setBorderLeft(BorderStyle.THIN);
			header_CellStyle.setBorderRight(BorderStyle.THIN);
			// 中央揃え
			header_CellStyle.setAlignment(HorizontalAlignment.CENTER);
			// フォントをセット
			header_CellStyle.setFont(font);
			// column_Names 分ループ
			Row row = sheet.createRow(0);
			for (int i = 0; i < column_Names.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(column_Names[i]);
				// セルにセルスタイルを適用
				cell.setCellStyle(header_CellStyle);
				if (column_Width != null) {
					// 列幅設定（1文字分の横幅 × 文字数 ＋ 微調整分の幅）
					sheet.setColumnWidth(i, 512 * column_Width[i] + 0);
				}
			}
			// データ行
			// セルスタイルを定義
			CellStyle data_CellStyle = workbook.createCellStyle();
			// 罫線指定
			data_CellStyle.setBorderTop(BorderStyle.THIN);
			data_CellStyle.setBorderBottom(BorderStyle.THIN);
			data_CellStyle.setBorderLeft(BorderStyle.THIN);
			data_CellStyle.setBorderRight(BorderStyle.THIN);
			// フォントをセット
			data_CellStyle.setFont(font);
			for (int i = 0; i < table_Data.length; i++) {
				// 行を指定
				row = sheet.createRow(i + 1);
				for (int j = 0; j < table_Data[i].length; j++) {
					// セルを定義
					Cell cell = row.createCell(j);
					String value = table_Data[i][j];
					// セルに値をセット
					if(is_Double(value)){
						cell.setCellValue(Double.parseDouble(value));
					} else {
						cell.setCellValue(value);
					}
					// セルにセルスタイルを適用
					cell.setCellStyle(data_CellStyle);
					// 最後の行のみ
					if (i == table_Data.length - 1) {
						// 列幅設定（オート）
						sheet.autoSizeColumn(j);
					}
				}
			}
	    // ファイル名を指定して保存
			String encodedFilename = URLEncoder.encode(file_Name, "UTF-8");
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    response.setHeader("Content-Disposition", "attachment;filename=\"" + encodedFilename + "\"");
	    response.setCharacterEncoding("UTF-8");
	    workbook.write(outputStream);
			message += "が完了しました";
			workbook.close();
			message += " workbook を close() しました";
		} catch (IOException e) {
			message += "が正常に完了出来ませんでした";
			___console_Out___(e.getMessage());
		}
		___console_Out___("output_Excel() 終了");
		return message;
	}

	/** Table データを Excel として出力 (column_Width指定なし)*/
	public String output_Excel(String name_Head, String[] column_Names, String[][] table_Data, HttpServletResponse response) {
		return output_Excel(name_Head, column_Names, null, table_Data, response);
	}

	/** ファイル取得 */
	public File file(String file_Name) {
		File file = null;
		try {
			// Fileオブジェクトの生成
			file = new File(file_Name);
			// ファイルの存在を確認
			Boolean fileExists = file.exists();
			if (fileExists == false) {
				System.out.println("ファイル " + file_Name + "が無いので作成します");
				file = create_File(file_Name);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// Fileオブジェクトの生成
			file = new File(file_Name);
		}
		return file;
	}

	/** Excelファイル作成 */
	public File create_File(String file_Name) {
		File file = null;
		try {
			System.out.println("ファイル " + file_Name + "が無いので作成します");
			Workbook workbook = workbook();
//			// 出力用のストリームを用意
//			FileOutputStream out = new FileOutputStream(file_Name);
//			// ファイルへ出力
//			workbook.write(out);
			write(workbook, file_Name);
			file = new File(file_Name);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return file;
	}

	/** Excelファイルの書き込み */
	public boolean write(Workbook workbook, String file_Name) {
		boolean is_Safe = false;
		try (FileOutputStream fileOutputStream = new FileOutputStream(new File(file_Name))) {
			workbook.write(fileOutputStream);
			is_Safe = true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return is_Safe;
	}

	public boolean write(Workbook workbook, ServletOutputStream outputStream) {
		boolean is_Safe = false;
		try (outputStream) {
			workbook.write(outputStream);
			is_Safe = true;
			workbook.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return is_Safe;
	}

	/** Workbook 作成 */
	public Workbook workbook() {
		Workbook workbook = new XSSFWorkbook();
		return workbook;
	}

	/** Workbook 取得 */
	public Workbook workbook(String file_Name) {
		Workbook workbook = null;
		create_File(file_Name);
		try (FileInputStream file = new FileInputStream(new File(file_Name))) {
	    workbook = WorkbookFactory.create(file);
		} catch (Exception e) {
		  System.out.println(e.getMessage());
		}
		return workbook;
	}

	/** ワークシート 取得（Workbook から sheet_Name で取得） */
	public Sheet sheet(Workbook workbook, String sheet_Name) {
		Sheet sheet = workbook.createSheet(sheet_Name);
		return sheet;
	}

	/** ワークシート 取得（file_Name という名の Workbook から sheet_Name で取得） */
	public Sheet sheet(String file_Name, String sheet_Name) {
		Workbook workbook = workbook(file_Name);
		Sheet sheet = workbook.createSheet(sheet_Name);
		return sheet;
	}

	/** ワークシート 取得（Workbook から sheet_Number で取得） */
	public Sheet getSheetAt(Workbook workbook, int sheet_Number) {
		Sheet sheet = workbook.getSheetAt(sheet_Number);
		return sheet;
	}

	/** ワークシート 取得（file_Name という名の Workbook から sheet_Number で取得） */
	public Sheet getSheetAt(String file_Name, int sheet_Number) {
		Workbook workbook = workbook(file_Name);
		Sheet sheet = workbook.getSheetAt(sheet_Number);
		return sheet;
	}

	/** 行 取得 */
	public Row row(Sheet sheet, int row_Number) {
		Row row = sheet.createRow(row_Number);
		return row;
	}

	/** セル入力（行以下指定）*/
	public boolean setCellValue(Row row, int column_Number, String set_Value) {
		boolean is_Safe = false;
		try {
			Cell cell = row.createCell(column_Number);
			cell.setCellValue(set_Value);
			is_Safe = true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return is_Safe;
	}

	/** セル入力（シート以下指定）*/
	public boolean setCellValue(Sheet sheet, int row_Number, int column_Number, String set_Value) {
		boolean is_Safe = false;
		try {
			Row row = sheet.createRow(row_Number);
			Cell cell = row.createCell(column_Number);
			cell.setCellValue(set_Value);
			is_Safe = true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return is_Safe;
	}

	/** 列幅の設定 */
	public int setColumnWidth(Sheet sheet, int column_Number, int x_Word_Count, int tuning_Width) {
		int columnWidth = -1;
		try {
			// 1文字分の横幅 × 文字数 ＋ 微調整分の幅
			columnWidth = 256 * x_Word_Count + tuning_Width;
			sheet.setColumnWidth(column_Number - 1, columnWidth);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			columnWidth = -1;
		}
		return columnWidth;
	}

	/** 行の高さ設定 */
	public float setColumnWidth(Sheet sheet, int row_Number, float height) {
		try {
			Row row = sheet.createRow(row_Number);
			row.setHeightInPoints(height);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			height = -1;
		}
		return height;
	}

	/** セルのデータ書式設定 */
	public String setCellStyle(Cell cell, String format_Pattern) {
		try {
			if (format_Pattern == null) format_Pattern = "#,##0.00";
			Workbook workbook = workbook();
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setDataFormat(workbook.createDataFormat().getFormat(format_Pattern));
			cell.setCellStyle(cellStyle);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			format_Pattern = null;
		}
		return format_Pattern;
	}

	/** セルのフォントスタイル（太字）設定 */
	public boolean setBold(Cell cell, boolean use) {
		boolean is_Safe = false;
		try {
			Workbook workbook = workbook();
			CellStyle boldCellStyle = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setBold(use);
			boldCellStyle.setFont(font);
			cell.setCellStyle(boldCellStyle);
			is_Safe = true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return is_Safe;
	}

	//文字寄せ
	public String setAlignment(Cell cell,String horizontalAlignment) {
		HorizontalAlignment align = null;
		try {
			Workbook workbook = workbook();
			CellStyle centerCellStyle = workbook.createCellStyle();
			switch (horizontalAlignment) {
				case "CENTER":
					align = HorizontalAlignment.CENTER;
					break;
				case "LEFT":
					align = HorizontalAlignment.LEFT;
					break;
				case "RIGHT":
					align = HorizontalAlignment.RIGHT;
					break;
				case "FILL":
					align = HorizontalAlignment.FILL;
					break;
				case "JUSTIFY":
					align = HorizontalAlignment.JUSTIFY;
					break;
				default:
					align = HorizontalAlignment.LEFT;
			}
			centerCellStyle.setAlignment(align);
			cell.setCellStyle(centerCellStyle);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			horizontalAlignment = null;
		}
		return horizontalAlignment;
	}

	// 文字寄せ
	public String setAlignment(Cell cell,HorizontalAlignment horizontalAlignment) {
		String align = null;
		try {
			Workbook workbook = workbook();
			CellStyle centerCellStyle = workbook.createCellStyle();
			centerCellStyle.setAlignment(horizontalAlignment);
			cell.setCellStyle(centerCellStyle);
			align = horizontalAlignment.toString();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return align;
	}

	//色指定
	public String setFillForegroundColor(Cell cell, IndexedColors indexedColors) {
		String color_Name = null;
		try {
			Workbook workbook = workbook();
			CellStyle coloredCellStyle = workbook.createCellStyle();
			coloredCellStyle.setFillForegroundColor(indexedColors.getIndex());
			coloredCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cell.setCellStyle(coloredCellStyle);
			color_Name = indexedColors.toString();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return color_Name;
	}

	// 罫線指定
	public String setBorder(
			Cell cell,
			BorderStyle borderStyle_Top,
			BorderStyle borderStyle_Bottom,
			BorderStyle borderStyle_Left,
			BorderStyle borderStyle_Right) {
		String set_Border = null;
		try {
			Workbook workbook = workbook();
			CellStyle borderedCellStyle = workbook.createCellStyle();
			borderedCellStyle.setBorderTop(borderStyle_Top);
			borderedCellStyle.setBorderBottom(borderStyle_Bottom);
			borderedCellStyle.setBorderLeft(borderStyle_Left);
			borderedCellStyle.setBorderRight(borderStyle_Right);
			cell.setCellStyle(borderedCellStyle);
			set_Border =
					"BorderTop = " + borderStyle_Top.toString() +
					"BorderBottom = " + borderStyle_Bottom.toString() +
					"BorderLeft = " + borderStyle_Left.toString() +
					"BorderRight = " + borderStyle_Right.toString();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return set_Border;
	}

	/** 罫線指定(周り) */
	public static void border_Around(CellStyle cellStyle, BorderStyle borderStyle) {
		cellStyle.setBorderTop(borderStyle);
		cellStyle.setBorderBottom(borderStyle);
		cellStyle.setBorderLeft(borderStyle);
		cellStyle.setBorderRight(borderStyle);
	}

	public static void setCellValue(Cell cell, String value) {
		// セルに値をセット
		if(is_Double(value)){
			cell.setCellValue(Double.parseDouble(value));
		} else {
			cell.setCellValue(value.toString());
		}
	}

	private static boolean is_Double(String string) {
		boolean is_Double = true;
		try {
			___console_Out___("String = " + Double.parseDouble(string) + " は Double に変換出来ます");
		} catch (Exception e) {
			___console_Out___("string = " + string + " は Double に変換出来ません");
			is_Double = false;
		}
		return is_Double;
	}

	public String now() {
		// 現在日時を取得
		LocalDateTime now = LocalDateTime.now();
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
		return dateTimeFormatter.format(now);
	}

	private String with_Now(String head_String) {
		String now = now().replaceAll("[^0-9]", ""); // 現在日時の数字以外を "" に変換
//	String now = now().replaceAll("[^\\d]", "");  ←こちらでもOK
		now = now.substring(0, now.length()-3); // 後ろから3文字を取り除く
		return head_String + now;
	}

	// Font が font の String text の横幅（ピクセル単位）を返す
	public double getFontWidth(Font font, String text) {
		AttributedString attributedString = new AttributedString(text);
		attributedString.addAttribute(TextAttribute.FAMILY, font.getFontName(), 0, text.length());
		attributedString.addAttribute(TextAttribute.SIZE, (float)font.getFontHeightInPoints());
		if (font.getBold()) {
		    attributedString.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, 0, text.length());
		}
		if (font.getItalic()) {
		    attributedString.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE, 0, text.length());
		}
		FontRenderContext fontRenderContext = new FontRenderContext(null, true, true);
		TextLayout layout = new TextLayout(attributedString.getIterator(), fontRenderContext);
		Rectangle2D bounds = layout.getBounds();
		double width = bounds.getX() + bounds.getWidth();
		___console_Out___("width = " + width);
		return width;
	}

	/** コンソールに String を出力 */
	public static void ___console_Out___(String message) {
		System.out.println("");
		System.out.println(message);
		System.out.println("");
	}

}