USE [master]
GO

CREATE DATABASE [QuanLySieuThi]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'QuanLySieuThi', FILENAME = N'D:\DATA\QuanLySieuThi.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'QuanLySieuThi_log', FILENAME = N'D:\DATA\QuanLySieuThi_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [QuanLySieuThi] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [QuanLySieuThi].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [QuanLySieuThi] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET ARITHABORT OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [QuanLySieuThi] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [QuanLySieuThi] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET  ENABLE_BROKER 
GO
ALTER DATABASE [QuanLySieuThi] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [QuanLySieuThi] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET RECOVERY FULL 
GO
ALTER DATABASE [QuanLySieuThi] SET  MULTI_USER 
GO
ALTER DATABASE [QuanLySieuThi] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [QuanLySieuThi] SET DB_CHAINING OFF 
GO
ALTER DATABASE [QuanLySieuThi] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [QuanLySieuThi] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [QuanLySieuThi] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [QuanLySieuThi] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'QuanLySieuThi', N'ON'
GO
ALTER DATABASE [QuanLySieuThi] SET QUERY_STORE = OFF
GO
USE [QuanLySieuThi]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ChucVu](
	[MaCV] [varchar](255) NOT NULL,
	[Luong] [float] NULL,
	[TenCV] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[MaCV] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CT_HoaDon](
	[SoLuong] [int] NOT NULL,
	[maHD] [varchar](255) NOT NULL,
	[maSP] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[maSP] ASC,
	[maHD] ASC,
	[SoLuong] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HoaDon](
	[MaHD] [varchar](255) NOT NULL,
	[NgayBan] [datetime2](7) NULL,
	[khachHang_MaKH] [varchar](255) NULL,
	[nhanVien_MaNv] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[MaHD] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[KhachHang](
	[MaKH] [varchar](255) NOT NULL,
	[DiaChi] [nvarchar](200) NULL,
	[SDT] [varchar](255) NULL,
	[TenKH] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[MaKH] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LoaiSP](
	[MaLoaiSP] [varchar](255) NOT NULL,
	[TenLoaiSP] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[MaLoaiSP] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NhaCungCap](
	[MaNCC] [varchar](255) NOT NULL,
	[DiaChi] [nvarchar](200) NULL,
	[SoDT] [varchar](255) NULL,
	[TenNCC] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[MaNCC] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NhanVien](
	[MaNv] [varchar](255) NOT NULL,
	[CaLamViec] [varchar](255) NULL,
	[GioiTinh] [bit] NULL,
	[HoTen] [nvarchar](200) NULL,
	[Luong] [float] NULL,
	[NgaySinh] [datetime2](7) NULL,
	[SoDT] [varchar](255) NULL,
	[TrangThai] [nvarchar](200) NULL,
	[chucVu_MaCV] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[MaNv] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SanPham](
	[MaSP] [varchar](255) NOT NULL,
	[Anh] [varchar](255) NULL,
	[DonVi] [nvarchar](200) NULL,
	[Gia] [float] NULL,
	[SoLuong] [int] NULL,
	[TenSP] [nvarchar](200) NULL,
	[loaiSP_MaLoaiSP] [varchar](255) NULL,
	[nhaCungCap_MaNCC] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[MaSP] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TaiKhoan](
	[MaTK] [varchar](255) NOT NULL,
	[MatKhau] [varchar](255) NULL,
	[TaiKhoan] [varchar](255) NULL,
	[VaiTro] [varchar](255) NULL,
	[nhanVien_MaNv] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[MaTK] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
INSERT [dbo].[ChucVu] ([MaCV], [Luong], [TenCV]) VALUES (N'BH', 10000000, N'Bán Hàng')
INSERT [dbo].[ChucVu] ([MaCV], [Luong], [TenCV]) VALUES (N'QL', 25000000, N'Quản Lí')
INSERT [dbo].[ChucVu] ([MaCV], [Luong], [TenCV]) VALUES (N'TK', 15000000, N'Thống Kê')
GO
INSERT [dbo].[CT_HoaDon] ([SoLuong], [maHD], [maSP]) VALUES (5, N'HD00002', N'GVS00001')
INSERT [dbo].[CT_HoaDon] ([SoLuong], [maHD], [maSP]) VALUES (3, N'HD00001', N'IC00004')
INSERT [dbo].[CT_HoaDon] ([SoLuong], [maHD], [maSP]) VALUES (10, N'HD00003', N'IC00004')
INSERT [dbo].[CT_HoaDon] ([SoLuong], [maHD], [maSP]) VALUES (2, N'HD00002', N'MAL00007')
GO
INSERT [dbo].[HoaDon] ([MaHD], [NgayBan], [khachHang_MaKH], [nhanVien_MaNv]) VALUES (N'HD00001', CAST(N'2025-10-26T23:50:03.1460000' AS DateTime2), N'KH00002', N'BH00001')
INSERT [dbo].[HoaDon] ([MaHD], [NgayBan], [khachHang_MaKH], [nhanVien_MaNv]) VALUES (N'HD00002', CAST(N'2025-10-26T23:50:03.1460000' AS DateTime2), N'KH00001', N'BH00001')
INSERT [dbo].[HoaDon] ([MaHD], [NgayBan], [khachHang_MaKH], [nhanVien_MaNv]) VALUES (N'HD00003', CAST(N'2025-10-27T23:50:03.1460000' AS DateTime2), N'KH00005', N'BH00001')
GO
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00001', N'An Giang', N'0385937218', N'Nguyễn Thị Yến Linh')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00002', N'TP.HCM', N'0983758821', N'Ngô Thanh Hoài')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00003', N'Hậu Giang', N'0399473032', N'Trần Thanh Phong')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00004', N'Tiền Giang', N'0948593232', N'Dương Yến Nhi')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00005', N'Hà Nội', N'0398471923', N'Mai Tuyết Yên')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00006', N'Bà Rịa Vũng Tàu', N'0361892791', N'Bắc Trường Thanh')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00007', N'Bạc Liêu', N'0389132102', N'Lê Thị Hồng Yên')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00008', N'Tây Nguyên', N'0987543521', N'Lê Thị Minh Huyền')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00009', N'Cần Thơ', N'0984732812', N'Dương Đài Loan')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00010', N'Nghệ An', N'0387130951', N'Dương Tiểu Ngọc')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00011', N'TP.HCM', N'0398172312', N'Phạm Thanh Minh')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00012', N'Bình Dương', N'0893210839', N'Ngô Minh Tân')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00013', N'Đồng Tháp', N'0389271239', N'Mai Ngọc Hoàng')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00014', N'An Giang', N'0986438731', N'Lê Thị Kim Hoa')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00015', N'Cà Mau', N'0987473623', N'Nguyễn Thị Huyền Trân')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00016', N'Đà Nẵng', N'0987480932', N'Nguyễn Minh Hiếu')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00017', N'Bình Dương', N'0984732731', N'Phan Thị Ngọc Thêm')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00018', N'Bình Dương', N'0397439122', N'Phan Thị Ngọc Đẹp')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00019', N'Quy Nhơn', N'0394832731', N'Ngô Việt Hoàng')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00020', N'An Giang', N'0361231312', N'Nguyễn Bá Thịnh')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00021', N'Long An', N'0395121232', N'Ngô Xuân Thành')
INSERT [dbo].[KhachHang] ([MaKH], [DiaChi], [SDT], [TenKH]) VALUES (N'KH00022', N'TP. HCM', N'0391235123', N'Nguyễn Ngọc Trân')
GO
INSERT [dbo].[LoaiSP] ([MaLoaiSP], [TenLoaiSP]) VALUES (N'GVS', N'Giấy Vệ Sinh')
INSERT [dbo].[LoaiSP] ([MaLoaiSP], [TenLoaiSP]) VALUES (N'IC', N'Kem')
INSERT [dbo].[LoaiSP] ([MaLoaiSP], [TenLoaiSP]) VALUES (N'MAL', N'Mì Ăn Liền')
INSERT [dbo].[LoaiSP] ([MaLoaiSP], [TenLoaiSP]) VALUES (N'NG', N'Nước Giặt')
INSERT [dbo].[LoaiSP] ([MaLoaiSP], [TenLoaiSP]) VALUES (N'NM', N'Nước Mắm')
INSERT [dbo].[LoaiSP] ([MaLoaiSP], [TenLoaiSP]) VALUES (N'NN', N'Nước Ngọt')
INSERT [dbo].[LoaiSP] ([MaLoaiSP], [TenLoaiSP]) VALUES (N'ST', N'Sữa Tươi')
INSERT [dbo].[LoaiSP] ([MaLoaiSP], [TenLoaiSP]) VALUES (N'TH', N'Thịt Heo')
GO
INSERT [dbo].[NhaCungCap] ([MaNCC], [DiaChi], [SoDT], [TenNCC]) VALUES (N'JPF', N'Số 404, đường Nguyễn Trãi, Phường 8, Quận 5, TPHCM.', N'0398761922', N'J-P Fashion')
INSERT [dbo].[NhaCungCap] ([MaNCC], [DiaChi], [SoDT], [TenNCC]) VALUES (N'KF', N'Số 400 Nguyễn Trãi, Phường 8, Quận 5, TPHCM.', N'0985737288', N'Kboy – Fashion')
INSERT [dbo].[NhaCungCap] ([MaNCC], [DiaChi], [SoDT], [TenNCC]) VALUES (N'SS', N'Ngụ tại 41 Nguyễn Trãi, phường 7, Q. 5', N'0398571495', N'Su Su')
GO
INSERT [dbo].[NhanVien] ([MaNv], [CaLamViec], [GioiTinh], [HoTen], [Luong], [NgaySinh], [SoDT], [TrangThai], [chucVu_MaCV]) VALUES (N'BH00001', N'Ca 1', 1, N'vuthai1', 10000000, CAST(N'2001-02-16T00:00:00.0000000' AS DateTime2), N'0389131312', N'Đang làm', N'BH')
INSERT [dbo].[NhanVien] ([MaNv], [CaLamViec], [GioiTinh], [HoTen], [Luong], [NgaySinh], [SoDT], [TrangThai], [chucVu_MaCV]) VALUES (N'BH00002', N'Ca 1', 1, N'Nguyễn Bá Thịnh', 10000000, CAST(N'1998-07-11T00:00:00.0000000' AS DateTime2), N'0391238123', N'Nghỉ việc', N'BH')
INSERT [dbo].[NhanVien] ([MaNv], [CaLamViec], [GioiTinh], [HoTen], [Luong], [NgaySinh], [SoDT], [TrangThai], [chucVu_MaCV]) VALUES (N'BH00003', N'Ca 1', 0, N'Lê Thị Mỹ Thọ', 10000000, CAST(N'2001-11-21T00:00:00.0000000' AS DateTime2), N'0361892131', N'Đang làm', N'BH')
INSERT [dbo].[NhanVien] ([MaNv], [CaLamViec], [GioiTinh], [HoTen], [Luong], [NgaySinh], [SoDT], [TrangThai], [chucVu_MaCV]) VALUES (N'QL00001', N'Ca 2', 1, N'vuthai2', 25000000, CAST(N'2001-08-25T00:00:00.0000000' AS DateTime2), N'0333333333', N'Đang làm', N'QL')
INSERT [dbo].[NhanVien] ([MaNv], [CaLamViec], [GioiTinh], [HoTen], [Luong], [NgaySinh], [SoDT], [TrangThai], [chucVu_MaCV]) VALUES (N'TK00001', N'Ca 2', 1, N'Nguyễn Đình Trường', 15000000, CAST(N'2001-03-24T00:00:00.0000000' AS DateTime2), N'0312312331', N'Đang làm', N'TK')
GO
INSERT [dbo].[SanPham] ([MaSP], [Anh], [DonVi], [Gia], [SoLuong], [TenSP], [loaiSP_MaLoaiSP], [nhaCungCap_MaNCC]) VALUES (N'GVS00001', N'D:\Nam4.1\LTPT\TestCode\QuanLySieuThi_Client\src\main\java\ProductImage\cuon-bless-you-a-la-vie-nen-dep-xinh.jpg', N'Cuộn', 98000, 100, N'Giấy Vệ Sinh Bless', N'GVS', N'KF')
INSERT [dbo].[SanPham] ([MaSP], [Anh], [DonVi], [Gia], [SoLuong], [TenSP], [loaiSP_MaLoaiSP], [nhaCungCap_MaNCC]) VALUES (N'GVS00002', N'D:\Nam4.1\LTPT\TestCode\QuanLySieuThi_Client\src\main\java\ProductImage\giayvesinhmay.jfif', N'Cuộn', 78000, 200, N'Giấy Vệ Sinh May', N'GVS', N'JPF')
INSERT [dbo].[SanPham] ([MaSP], [Anh], [DonVi], [Gia], [SoLuong], [TenSP], [loaiSP_MaLoaiSP], [nhaCungCap_MaNCC]) VALUES (N'GVS00003', N'D:\Nam4.1\LTPT\TestCode\QuanLySieuThi_Client\src\main\java\ProductImage\giayvesinhmylan.jfif', N'Cuộn', 58000, 200, N'Giấy Vệ Sinh MyLan', N'GVS', N'JPF')
INSERT [dbo].[SanPham] ([MaSP], [Anh], [DonVi], [Gia], [SoLuong], [TenSP], [loaiSP_MaLoaiSP], [nhaCungCap_MaNCC]) VALUES (N'IC00004', N'D:\Nam4.1\LTPT\TestCode\QuanLySieuThi_Client\src\main\java\ProductImage\kemdau.jpeg', N'Cây', 10000, 90, N'Kem Dâu', N'IC', N'JPF')
INSERT [dbo].[SanPham] ([MaSP], [Anh], [DonVi], [Gia], [SoLuong], [TenSP], [loaiSP_MaLoaiSP], [nhaCungCap_MaNCC]) VALUES (N'IC00005', N'D:\Nam4.1\LTPT\TestCode\QuanLySieuThi_Client\src\main\java\ProductImage\Chocolate-Ice-Cream-200x200.jpg', N'Cây', 15000, 200, N'Kem Socola', N'IC', N'KF')
INSERT [dbo].[SanPham] ([MaSP], [Anh], [DonVi], [Gia], [SoLuong], [TenSP], [loaiSP_MaLoaiSP], [nhaCungCap_MaNCC]) VALUES (N'IC00006', N'D:\Nam4.1\LTPT\TestCode\QuanLySieuThi_Client\src\main\java\ProductImage\Lemon-Ice-Cream200x200.jpg', N'Cây', 12000, 150, N'Kem Chanh', N'IC', N'SS')
INSERT [dbo].[SanPham] ([MaSP], [Anh], [DonVi], [Gia], [SoLuong], [TenSP], [loaiSP_MaLoaiSP], [nhaCungCap_MaNCC]) VALUES (N'MAL00007', N'D:\Nam4.1\LTPT\TestCode\QuanLySieuThi_Client\src\main\java\ProductImage\mihaohao.jfif', N'Gói', 4000, 1500, N'Mì Hảo Hảo', N'MAL', N'KF')
INSERT [dbo].[SanPham] ([MaSP], [Anh], [DonVi], [Gia], [SoLuong], [TenSP], [loaiSP_MaLoaiSP], [nhaCungCap_MaNCC]) VALUES (N'MAL00008', N'D:\Nam4.1\LTPT\TestCode\QuanLySieuThi_Client\src\main\java\ProductImage\mi-ga-tim-1-700x467-1-200x200.jpg', N'Gói', 4500, 1500, N'Mì Gà Tím', N'MAL', N'JPF')
GO
INSERT [dbo].[TaiKhoan] ([MaTK], [MatKhau], [TaiKhoan], [VaiTro], [nhanVien_MaNv]) VALUES (N'TK00001', N'thaiql', N'thaiql', N'QL', N'QL00001')
INSERT [dbo].[TaiKhoan] ([MaTK], [MatKhau], [TaiKhoan], [VaiTro], [nhanVien_MaNv]) VALUES (N'TK00002', N'thaibh', N'thaibh', N'BH', N'BH00001')
INSERT [dbo].[TaiKhoan] ([MaTK], [MatKhau], [TaiKhoan], [VaiTro], [nhanVien_MaNv]) VALUES (N'TK00003', N'thaitk', N'thaitk', N'TK', N'TK00001')
GO
ALTER TABLE [dbo].[CT_HoaDon]  WITH CHECK ADD  CONSTRAINT [FKb15jxw96howv7ox8nejbhvm4s] FOREIGN KEY([maSP])
REFERENCES [dbo].[SanPham] ([MaSP])
GO
ALTER TABLE [dbo].[CT_HoaDon] CHECK CONSTRAINT [FKb15jxw96howv7ox8nejbhvm4s]
GO
ALTER TABLE [dbo].[CT_HoaDon]  WITH CHECK ADD  CONSTRAINT [FKnbc1n418i6dvdy4iip69upge7] FOREIGN KEY([maHD])
REFERENCES [dbo].[HoaDon] ([MaHD])
GO
ALTER TABLE [dbo].[CT_HoaDon] CHECK CONSTRAINT [FKnbc1n418i6dvdy4iip69upge7]
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD  CONSTRAINT [FK6m9umm55svqydwflrnqsrbr88] FOREIGN KEY([nhanVien_MaNv])
REFERENCES [dbo].[NhanVien] ([MaNv])
GO
ALTER TABLE [dbo].[HoaDon] CHECK CONSTRAINT [FK6m9umm55svqydwflrnqsrbr88]
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD  CONSTRAINT [FKf0rk3q9r6h1agvlgol1da6eky] FOREIGN KEY([khachHang_MaKH])
REFERENCES [dbo].[KhachHang] ([MaKH])
GO
ALTER TABLE [dbo].[HoaDon] CHECK CONSTRAINT [FKf0rk3q9r6h1agvlgol1da6eky]
GO
ALTER TABLE [dbo].[NhanVien]  WITH CHECK ADD  CONSTRAINT [FKil2gtl2hu10l0oqel4x6q3v5l] FOREIGN KEY([chucVu_MaCV])
REFERENCES [dbo].[ChucVu] ([MaCV])
GO
ALTER TABLE [dbo].[NhanVien] CHECK CONSTRAINT [FKil2gtl2hu10l0oqel4x6q3v5l]
GO
ALTER TABLE [dbo].[SanPham]  WITH CHECK ADD  CONSTRAINT [FKjj0hub0ewiaxbbbjlygq4rqwq] FOREIGN KEY([nhaCungCap_MaNCC])
REFERENCES [dbo].[NhaCungCap] ([MaNCC])
GO
ALTER TABLE [dbo].[SanPham] CHECK CONSTRAINT [FKjj0hub0ewiaxbbbjlygq4rqwq]
GO
ALTER TABLE [dbo].[SanPham]  WITH CHECK ADD  CONSTRAINT [FKk5vq8vobu9q3bnlqftjtbhfc6] FOREIGN KEY([loaiSP_MaLoaiSP])
REFERENCES [dbo].[LoaiSP] ([MaLoaiSP])
GO
ALTER TABLE [dbo].[SanPham] CHECK CONSTRAINT [FKk5vq8vobu9q3bnlqftjtbhfc6]
GO
ALTER TABLE [dbo].[TaiKhoan]  WITH CHECK ADD  CONSTRAINT [FKb9d9f8kt7sp5hffkvee686qxk] FOREIGN KEY([nhanVien_MaNv])
REFERENCES [dbo].[NhanVien] ([MaNv])
GO
ALTER TABLE [dbo].[TaiKhoan] CHECK CONSTRAINT [FKb9d9f8kt7sp5hffkvee686qxk]
GO
USE [master]
GO
ALTER DATABASE [QuanLySieuThi] SET  READ_WRITE 
GO
