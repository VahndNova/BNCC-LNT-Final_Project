import java.util.Random;
import java.util.Scanner;
import java.sql.*;

public class Main {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/pudding_menu";
	static Connection conn = null;
	static Statement stmt = null;
	static Scanner input = new Scanner(System.in);
	static Random random = new Random();

	public static void main(String[] args) {
		try {
			Class.forName(JDBC_DRIVER);

			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL);

			System.out.println("Connected to database.");

			boolean exit = false;
			while (!exit) {
				System.out.println("\nMenu:");
				System.out.println("1. Insert Menu Baru");
				System.out.println("2. View Menu");
				System.out.println("3. Update Menu");
				System.out.println("4. Delete Menu");
				System.out.println("5. Keluar");

				System.out.print("Pilih menu: ");
				int choice = input.nextInt();

				switch (choice) {
				case 1:
					insertMenu();
					break;
				case 2:
					viewMenu();
					break;
				case 3:
					updateMenu();
					break;
				case 4:
					deleteMenu();
					break;
				case 5:
					exit = true;
					System.out.println("Terima kasih.");
					break;
				default:
					System.out.println("Menu yang dipilih tidak tersedia.");
				}
			}

			conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public static void insertMenu() {
		try {
			System.out.println("\nInsert Menu Baru");

			String kodeMenu = "PD-" + random.nextInt(999);
			System.out.println("Kode Menu: " + kodeMenu);

			System.out.print("Nama Menu: ");
			String namaMenu = input.next();

			System.out.print("Harga Menu: ");
			double hargaMenu = input.nextDouble();

			System.out.print("Stok Menu: ");
			int stokMenu = input.nextInt();

			stmt = conn.createStatement();
			String sql = "INSERT INTO menu (kode_menu, nama_menu, harga_menu, stok_menu) " + "VALUES ('" + kodeMenu
					+ "', '" + namaMenu + "', " + hargaMenu + ", " + stokMenu + ")";
			stmt.executeUpdate(sql);

			System.out.println("Menu berhasil ditambahkan.");
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	public static void viewMenu() {
		try {
			System.out.println("\nView Menu");

			stmt = conn.createStatement();
			String sql = "SELECT * FROM menu";
			ResultSet rs = stmt.executeQuery(sql);

			System.out.printf("%-10s %-20s %-10s %-10s\n", "Kode Menu", "Nama Menu", "Harga Menu", "Stok Menu");

			while (rs.next()) {
				String kodeMenu = rs.getString("kode_menu");
				String namaMenu = rs.getString("nama_menu");
				double hargaMenu = rs.getDouble("harga_menu");
				int stokMenu = rs.getInt("stok_menu");
				System.out.printf("%-10s %-20s %-10.2f %-10d\n", kodeMenu, namaMenu, hargaMenu, stokMenu);
			}

			rs.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	public static void updateMenu() {
		try {
			System.out.println("\nUpdate Menu");

			System.out.print("Masukkan kode menu: ");
			String kodeMenu = input.next();

			stmt = conn.createStatement();
			String sql = "SELECT * FROM menu WHERE kode_menu = '" + kodeMenu + "'";
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				System.out.printf("%-10s %-20s %-10s %-10s\n", "Kode Menu", "Nama Menu", "Harga Menu", "Stok Menu");

				String namaMenu = rs.getString("nama_menu");
				double hargaMenu = rs.getDouble("harga_menu");
				int stokMenu = rs.getInt("stok_menu");

				System.out.printf("%-10s %-20s %-10.2f %-10d\n", kodeMenu, namaMenu, hargaMenu, stokMenu);

				System.out.print("Masukkan harga menu baru: ");
				double newHargaMenu = input.nextDouble();

				System.out.print("Masukkan stok menu baru: ");
				int newStokMenu = input.nextInt();

				sql = "UPDATE menu SET harga_menu = " + newHargaMenu + ", stok_menu = " + newStokMenu
						+ " WHERE kode_menu = '" + kodeMenu + "'";
				stmt.executeUpdate(sql);

				System.out.println("Menu berhasil diupdate.");
			} else {
				System.out.println("Kode menu tidak ditemukan.");
			}

			rs.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	public static void deleteMenu() {
		try {
			System.out.println("\nDelete Menu");

			System.out.print("Masukkan kode menu: ");
			String kodeMenu = input.next();

			stmt = conn.createStatement();
			String sql = "SELECT * FROM menu WHERE kode_menu = '" + kodeMenu + "'";
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				System.out.printf("%-10s %-20s %-10s %-10s\n", "Kode Menu", "Nama Menu", "Harga Menu", "Stok Menu");

				String namaMenu = rs.getString("nama_menu");
				double hargaMenu = rs.getDouble("harga_menu");
				int stokMenu = rs.getInt("stok_menu");

				System.out.printf("%-10s %-20s %-10.2f %-10d\n", kodeMenu, namaMenu, hargaMenu, stokMenu);

				System.out.print("Apakah anda yakin ingin menghapus menu ini? (Y/N) ");
				String confirmation = input.next();

				if (confirmation.equalsIgnoreCase("Y")) {
					sql = "DELETE FROM menu WHERE kode_menu = '" + kodeMenu + "'";
					stmt.executeUpdate(sql);

					System.out.println("Menu berhasil dihapus.");
				} else {
					System.out.println("Menu tidak jadi dihapus.");
				}
			} else {
				System.out.println("Kode menu tidak ditemukan.");
			}

			rs.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

}
