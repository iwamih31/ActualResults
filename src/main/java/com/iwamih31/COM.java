package com.iwamih31;

import java.lang.reflect.Method;

public class COM {

	/** コンソールに『クラス名.メソッド名 開始』と出力 */
  public void ___method_Start___() {
  	StackWalker stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
  	Class<?> callerClass = stackWalker.getCallerClass();
  	Method enclosingMethod = callerClass.getEnclosingMethod();
  	___console_Out___(callerClass.getName() + "." + enclosingMethod.getName() + " 開始");
  }

	/** コンソールに『クラス名.メソッド名 終了』と出力 */
  public void ___method_End___() {
  	StackWalker stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
  	Class<?> callerClass = stackWalker.getCallerClass();
  	Method enclosingMethod = callerClass.getEnclosingMethod();
  	___console_Out___(callerClass.getName() + "." + enclosingMethod.getName() + " 終了");
  }

	/** コンソールに String message を出力（上下に空白行在り） */
	public void ___console_Out___(String message) {
		System.out.println("");
		System.out.println(message);
		System.out.println("");
	}

}
