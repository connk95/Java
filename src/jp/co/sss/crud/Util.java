package jp.co.sss.crud;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Util {
	private void executeQuery(String sql, QueryCallback callback) throws Exception, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// DBに接続
			connection = Db.getConnection();

			// ステートメントを作成
			preparedStatement = connection.prepareStatement(sql);

			// コールバックを実行する
			if (callback != null) {
				callback.apply(preparedStatement, resultSet);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			// クローズ処理
			Db.close(resultSet);
			Db.close(preparedStatement);
			Db.close(connection);
		}
	}

	/**
	 * 全ての社員情報を検索する
	 */
	public void showAll() throws Exception {
		String sql = "SELECT e.emp_id, e.emp_name, e.gender, e.birthday, d.dept_name FROM employee e INNER JOIN " + 
					"department d ON e.dept_id = d.dept_id ORDER BY emp_id";

		executeQuery(sql, (preparedStatement, resultSet) -> {
			resultSet = preparedStatement.executeQuery();
			// レコードを出力
			System.out.printf("%-8s %-11s %-5s %-10s %-10s%n", "社員ID", "社員名", "性別", "生年月日", "部署名");
			while (resultSet.next()) {
				System.out.printf("%-10s %-10s %-7s %-14s %-10s%n",
						resultSet.getString("emp_id"),
						resultSet.getString("emp_name"),
						resultSet.getString("gender"),
						resultSet.getString("birthday").substring(0, 10),
						resultSet.getString("dept_name"));
			}
		});
	}

	/**
	 * 社員名で社員情報を検索する
	 *
	 * @param empName 検索キーワード(社員名)
	 */
	public void searchName() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("社員名を入力してください:");
		String empName = br.readLine();

		String sql = "SELECT e.emp_id, e.emp_name, e.gender, e.birthday, d.dept_name FROM employee e INNER JOIN " + 
					"department d ON e.dept_id = d.dept_id WHERE emp_name LIKE ? ORDER BY emp_id";

		executeQuery(sql, (preparedStatement, resultSet) -> {
			preparedStatement.setString(1, "%" + empName + "%");
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet == null) {
				throw new Exception("入力した部署IDが存在しません");
			} else {
			// レコードを出力
			System.out.printf("%-8s %-11s %-5s %-10s %-10s%n", "社員ID", "社員名", "性別", "生年月日", "部署名");
			while (resultSet.next()) {
				System.out.printf("%-10s %-10s %-7s %-14s %-10s%n",
						resultSet.getString("emp_id"),
						resultSet.getString("emp_name"),
						resultSet.getString("gender"),
						resultSet.getString("birthday").substring(0, 10),
						resultSet.getString("dept_name"));
				}
			}
		});
	}

	/**
	 * 社員IDで社員情報を検索する
	 *
	 * @param deptId 検索キーワード(部署ID)
	 */
	public void searchId() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("部署ID(1：営業部、2：経理部、3：総務部)を入力してください:");
		String deptId = br.readLine();

		String sql = "SELECT e.emp_id, e.emp_name, e.gender, e.birthday, d.dept_name FROM employee e INNER JOIN " + 
					"department d ON e.dept_id = d.dept_id WHERE e.dept_id = ? ORDER BY emp_id";

		executeQuery(sql, (preparedStatement, resultSet) -> {
			preparedStatement.setString(1, deptId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet == null) {
				throw new Exception("入力した部署IDが存在しません");
			} else {
				// レコードを出力
				System.out.printf("%-8s %-11s %-5s %-10s %-10s%n", "社員ID", "社員名", "性別", "生年月日", "部署名");
				while (resultSet.next()) {
					System.out.printf("%-10s %-10s %-7s %-14s %-10s%n",
							resultSet.getString("emp_id"),
							resultSet.getString("emp_name"),
							resultSet.getString("gender"),
							resultSet.getString("birthday").substring(0, 10),
							resultSet.getString("dept_name"));
				}
			}
		});
	}

	/**
	 * 社員情報を登録する
	 */
	public void insert() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("社員名:");
		String empName = br.readLine();
		System.out.println("性別(1: 男性, 2: 女性):");
		String gender = br.readLine();
		System.out.println("生年月日（西暦年/月/日）:");
		String birthday = br.readLine();
		System.out.println("部署ID(1：営業部、2：経理部、3：総務部):");
		String dept = br.readLine();

		String sql = "INSERT INTO employee (emp_id, emp_name, gender, birthday, dept_id) VALUES " + 
					"(seq_emp.NEXTVAL, ?, ?, ?, ?)";

		executeQuery(sql, (preparedStatement, resultSet) -> {
			preparedStatement.setString(1, empName);
			preparedStatement.setString(2, gender);
			preparedStatement.setString(3, birthday);
			preparedStatement.setString(4, dept);

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				throw new Exception("入力した社員IDがもう存在しています");
			} else {
				System.out.println("社員情報を登録しました");
			}
		});
	}

	/**
	 * 社員情報を更新する
	 */
	public void update() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("更新する社員の社員IDを入力してください:");
		String empId = br.readLine();

		System.out.println("社員名:");
		String empName = br.readLine();
		System.out.println("性別(1: 男性, 2: 女性):");
		String gender = br.readLine();
		System.out.println("生年月日（西暦年/月/日）:");
		String birthday = br.readLine();
		System.out.println("部署ID(1：営業部、2：経理部、3：総務部):");
		String dept = br.readLine();

		String sql = "UPDATE employee SET emp_name = ?, gender = ?, birthday = ?, dept_id = ? WHERE emp_id = ?";
		executeQuery(sql, (preparedStatement, resultSet) -> {
			preparedStatement.setString(1, empName);
			preparedStatement.setString(2, gender);
			preparedStatement.setString(3, birthday);
			preparedStatement.setString(4, dept);
			preparedStatement.setString(5, empId);

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				throw new Exception("入力した社員が存在しません");
			} else {
				System.out.println("社員情報を更新しました");
			}
		});
	}

	/**
	 * 社員情報を削除する
	 */
	public void delete() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("削除する社員の社員IDを入力してください:");
		String empId = br.readLine();

		String sql = "DELETE FROM employee WHERE emp_id = ?";

		executeQuery(sql, (preparedStatement, resultSet) -> {
			preparedStatement.setString(1, empId);

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				throw new Exception("入力した社員が存在しません");
			} else {
				System.out.println("社員情報を削除しました");
			}
		});
	}

	interface QueryCallback {
		void apply(PreparedStatement preparedStatement, ResultSet resultSet) throws Exception;
	}
}
