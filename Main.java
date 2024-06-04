/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author ADMIN
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    static Connection connection = null;
    public static void connect() throws SQLException
    {
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true;"+
                "encrypt=true;trustServerCertificate=true;";
        //String connectionUrl = "jdbc:sqlserver://LAPTOP-5KMVI2A8\\SQLEXPRESS:1433;databaseName=CSDLhanghoa;user=User2;password=123;integratedSecurity=true;"+
        //"encrypt=true;trustServerCertificate=true;";
        // Đăng ký driver
            
        connection = DriverManager.getConnection(connectionUrl);
    }
    public static void getData() throws SQLException{
        Statement statement = null;
        ResultSet resultSet = null;
        statement = connection.createStatement();
        // Thực thi câu lệnh SQL
        String sql = "SELECT * FROM CHITIETHD, HANGHOA";
        resultSet = statement.executeQuery(sql);
        // Xử lý kết quả
        while (resultSet.next()) {
            System.out.println("MaHD: " + resultSet.getString("MaHD"));
            System.out.println("MaH:"+  resultSet.getString("MaH"));
            System.out.println("SoLuong:"+  resultSet.getString("SoLuong"));

            System.out.println("MaH: " + resultSet.getString("MaH"));
            System.out.println("MaH:"+  resultSet.getString("MaH"));
            System.out.println("DonViTinh:"+  resultSet.getString("DonViTinh"));
            System.out.println("DonGia:"+  resultSet.getString("DonGia"));
            System.out.println("----------------------------");

        }

        if (resultSet != null) resultSet.close();
        
        if (statement != null) statement.close();
        
    }
    public static void exit() throws SQLException{
       if (connection != null) connection.close();
    }

    public static void editData(String MaHD, String MaH, String SoLuong) throws SQLException {
    Scanner input = new Scanner(System.in);
    boolean isDuplicateMaH = true;

    while (isDuplicateMaH) {
        // Check if MaH exists (excluding the row being edited)
        String checkMaHQuery = "SELECT * FROM CHITIETHD WHERE MaH = ? AND MaHD != ?";
        PreparedStatement checkMaHStatement = connection.prepareStatement(checkMaHQuery);
        checkMaHStatement.setString(1, MaH);
        checkMaHStatement.setString(2, MaHD);
        ResultSet checkMaHResult = checkMaHStatement.executeQuery();

        if (checkMaHResult.next())
        {
            System.out.println("MaH đã tồn tại. Vui lòng nhập lại MaH mới: ");
            MaH = input.nextLine();
        } else {
            // MaH is unique, proceed with the edit
            isDuplicateMaH = false;
        }

        checkMaHStatement.close();
        checkMaHResult.close();
    }

    // Perform the actual edit using the updated MaH
    String editQuery = "UPDATE CHITIETHD SET MaH = ?, SoLuong = ? WHERE MaHD = ?";
    PreparedStatement editStatement = connection.prepareStatement(editQuery);
    editStatement.setString(1, MaH);
    editStatement.setString(2, SoLuong);
    editStatement.setString(3, MaHD);
    editStatement.executeUpdate();
    System.out.println("Cập nhật thành công!");

    editStatement.close();
}


    public static void addData(String MaHD, String MaH, String SoLuong) throws SQLException{
        String query="insert into CHITIETHD values(?,?,?)";
        PreparedStatement pstmt=null;
        try{
        pstmt=connection.prepareStatement(query);
        pstmt.setString(1,MaHD);
        pstmt.setString(2,MaH);
        pstmt.setString(3,SoLuong);

        pstmt.executeUpdate();
        System.out.println("Them thanh cong!");
        }
        catch(SQLException e){
            System.out.println("Loi"+e.getMessage());
            
        }
        
        if (pstmt != null) pstmt.close();
    }
    public static void delData(String MaHD) throws SQLException {
        // Check for dependent records using foreign key constraints
        String checkDependentRecordsQuery = "SELECT * FROM HANGHOA WHERE MaH = ?";
        PreparedStatement checkDependentRecordsStatement = connection.prepareStatement(checkDependentRecordsQuery);
        checkDependentRecordsStatement.setString(1, MaHD);
        ResultSet checkDependentRecordsResult = checkDependentRecordsStatement.executeQuery();

        if (checkDependentRecordsResult.next()) {
            // Dependent records found, cannot delete
            System.out.println("Lỗi! Không thể xóa hàng do có bản ghi phụ thuộc trong bảng HANGHOA. Vui lòng xóa bản ghi phụ thuộc trước.");
        }
        else {
            // No dependent records, proceed with the delete
            String deleteQuery = "DELETE FROM CHITIETHD WHERE MaHD = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setString(1, MaHD);
            int deletedRows = deleteStatement.executeUpdate();

            if (deletedRows == 1) {
                System.out.println("Xóa thành công!");
            } else {
                System.out.println("Lỗi! Không thể xóa bản ghi.");
            }

            deleteStatement.close();
    }

    checkDependentRecordsStatement.close();
    checkDependentRecordsResult.close();
}


    public static void searchData(String MaHD) throws SQLException{
        String query="select * from CHITIETHD where MaHD=?";
        PreparedStatement pstmt=null;
        ResultSet resultSet = null;
        try{
        pstmt=connection.prepareStatement(query);
        pstmt.setString(1,MaHD);
        
        resultSet = pstmt.executeQuery();
        // Xử lý kết quả
        while (resultSet.next()) {
            System.out.println("MaH: " + resultSet.getString("MaH"));
            System.out.println("SoLuong"+  resultSet.getString("SoLuong"));


        }

        }
        catch(SQLException e){
            System.out.println("Loi"+e.getMessage());
            
        }
        if(resultSet!=null) resultSet.close();
        if (pstmt != null) pstmt.close();
    }
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
        connect();
        Scanner input=new Scanner(System.in);
        boolean lap=true;
        while(lap==true){
            System.out.println("""
                               1. Hien thi danh sach loai hang
                               2. Them danh sach loai hang
                               3. Sua loai hang
                               4. Xoa loai hang
                               5. Tim kiem loai hang
                               6. Thoat
                               """);
            System.out.println("Moi chon:");
            int chon=input.nextInt();
            input.nextLine();
            switch (chon) {
                case 1:
                    getData();
                    break;
                case 2:
                    System.out.print("Nhap maHD:");
                    String MaHD=input.nextLine();
                    System.out.print("Nhap MaH:");
                    String MaH=input.nextLine();
                    System.out.print("Nhap so luong:");
                    String SoLuong=input.nextLine();
                    addData(MaHD, MaH, SoLuong);
                    break;
                case 3:
                    System.out.print("Nhap maHD can sua:");
                    MaHD=input.nextLine();
                    System.out.print("Nhap maH moi:");
                    MaH=input.nextLine();
                    System.out.print("Nhap SoLuong moi:");
                    SoLuong=input.nextLine();
                    editData(MaHD,MaH, SoLuong);
                    break;
                case 4:
                    System.out.print("Nhap maHD:");
                    MaHD=input.next();
                    delData(MaHD);
                    break;
                case 5:
                    System.out.print("Nhap HD can tim:");
                    MaHD=input.nextLine();
                    searchData(MaHD);
                    break;
                case 6:
                    lap=false;
                    break;
                default:
                    System.out.println("Hay chon dung chuc nang");
            }
        }
        exit();
        
       
             
    }
    
}
