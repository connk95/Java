package jp.co.sss.crud;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
	public static void main(String[] args) throws Exception {

		boolean continueFlag = true;

		Util utility = new Util();

		// ユーザーが続けたいか確認する
		while (continueFlag) {

			System.out.println("=== 社員管理システム ===");
			System.out.println("1. 全件表示");
			System.out.println("2. 社員名検索");
			System.out.println("3. 部署ID検索");
			System.out.println("4. 登録");
			System.out.println("5. 更新");
			System.out.println("6. 削除");
			System.out.println("7. 終了");
			System.out.println("メニュー番号を入力してください:");

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String selection = br.readLine();
			int selectionNum = Integer.parseInt(selection);

			try {
				switch (selectionNum) {
				case 1:
					utility.showAll();
					break;
				case 2:
					utility.searchName();
					break;
				case 3:
					utility.searchId();
					break;
				case 4:
					utility.insert();
					break;
				case 5:
					utility.update();
					break;
				case 6:
					utility.delete();
					break;
				case 7:
					System.out.println("システムを終了します");
					continueFlag = false;
					break;
				default:
					System.out.println("有効な選択を入力してください");
					break;
				}
			} catch (Exception e) {
				System.out.println("システムエラーが発生しました");
				System.out.println(e);
				System.out.println("システムを終了します");
				continueFlag = false;
				break;
			}
		}
	}
}
