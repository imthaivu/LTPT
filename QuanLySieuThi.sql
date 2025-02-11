USE [master];
GO

CREATE DATABASE [QuanLySieuThi]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'QuanLySieuThi', FILENAME = N'/Users/levanduy/dataQuanLySieuThi.mdf', SIZE = 8192KB, MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'QuanLySieuThi_log', FILENAME = N'/Users/levanduy/dataQuanLySieuThi_log.ldf', SIZE = 8192KB, MAXSIZE = 2048GB, FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT;
GO

USE [QuanLySieuThi];
GO

SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
GO

CREATE TABLE [dbo].[ChucVu](
	[MaCV] [varchar](255) NOT NULL,
	[Luong] [float] NULL,
	[TenCV] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[MaCV] ASC
) ON [PRIMARY]
) ON [PRIMARY];
GO

SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
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
) ON [PRIMARY]
) ON [PRIMARY];
GO

SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
GO

CREATE TABLE [dbo].[HoaDon](
	[MaHD] [varchar](255) NOT NULL,
	[NgayBan] [datetime2](7) NULL,
	[khachHang_MaKH] [varchar](255) NULL,
	[nhanVien_MaNv] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[MaHD] ASC
) ON [PRIMARY]
) ON [PRIMARY];
GO

SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
GO

CREATE TABLE [dbo].[KhachHang](
	[MaKH] [varchar](255) NOT NULL,
	[DiaChi] [nvarchar](200) NULL,
	[SDT] [varchar](255) NULL,
	[TenKH] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[MaKH] ASC
) ON [PRIMARY]
) ON [PRIMARY];
GO

SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
GO

CREATE TABLE [dbo].[LoaiSP](
	[MaLoaiSP] [varchar](255) NOT NULL,
	[TenLoaiSP] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[MaLoaiSP] ASC
) ON [PRIMARY]
) ON [PRIMARY];
GO

SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
GO

CREATE TABLE [dbo].[NhaCungCap](
	[MaNCC] [varchar](255) NOT NULL,
	[DiaChi] [nvarchar](200) NULL,
	[SoDT] [varchar](255) NULL,
	[TenNCC] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[MaNCC] ASC
) ON [PRIMARY]
) ON [PRIMARY];
GO

SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
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
) ON [PRIMARY]
) ON [PRIMARY];
GO

SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
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
) ON [PRIMARY]
) ON [PRIMARY];
GO

SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
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
) ON [PRIMARY]
) ON [PRIMARY];
GO

ALTER TABLE [dbo].[CT_HoaDon]  WITH CHECK ADD  CONSTRAINT [FKb15jxw96howv7ox8nejbhvm4s] FOREIGN KEY([maSP])
REFERENCES [dbo].[SanPham] ([MaSP]);
GO
ALTER TABLE [dbo].[CT_HoaDon] CHECK CONSTRAINT [FKb15jxw96howv7ox8nejbhvm4s];
GO

ALTER TABLE [dbo].[CT_HoaDon]  WITH CHECK ADD  CONSTRAINT [FKnbc1n418i6dvdy4iip69upge7] FOREIGN KEY([maHD])
REFERENCES [dbo].[HoaDon] ([MaHD]);
GO
ALTER TABLE [dbo].[CT_HoaDon] CHECK CONSTRAINT [FKnbc1n418i6dvdy4iip69upge7];
GO

ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD  CONSTRAINT [FK6m9umm55svqydwflrnqsrbr88] FOREIGN KEY([nhanVien_MaNv])
REFERENCES [dbo].[NhanVien] ([MaNv]);
GO
ALTER TABLE [dbo].[HoaDon] CHECK CONSTRAINT [FK6m9umm55svqydwflrnqsrbr88];
GO

ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD  CONSTRAINT [FKf0rk3q9r6h1agvlgol1da6eky] FOREIGN KEY([khachHang_MaKH])
REFERENCES [dbo].[KhachHang] ([MaKH]);
GO
ALTER TABLE [dbo].[HoaDon] CHECK CONSTRAINT [FKf0rk3q9r6h1agvlgol1da6eky];
GO

ALTER TABLE [dbo].[NhanVien]  WITH CHECK ADD  CONSTRAINT [FKil2gtl2hu10l0oqel4x6q3v5l] FOREIGN KEY([chucVu_MaCV])
REFERENCES [dbo].[ChucVu] ([MaCV]);
GO
ALTER TABLE [dbo].[NhanVien] CHECK CONSTRAINT [FKil2gtl2hu10l0oqel4x6q3v5l];
GO

ALTER TABLE [dbo].[SanPham]  WITH CHECK ADD  CONSTRAINT [FKjj0hub0ewiaxbbbjlygq4rqwq] FOREIGN KEY([nhaCungCap_MaNCC])
REFERENCES [dbo].[NhaCungCap] ([MaNCC]);
GO
ALTER TABLE [dbo].[SanPham] CHECK CONSTRAINT [FKjj0hub0ewiaxbbbjlygq4rqwq];
GO

ALTER TABLE [dbo].[SanPham]  WITH CHECK ADD  CONSTRAINT [FKk5vq8vobu9q3bnlqftjtbhfc6] FOREIGN KEY([loaiSP_MaLoaiSP])
REFERENCES [dbo].[LoaiSP] ([MaLoaiSP]);
GO
ALTER TABLE [dbo].[SanPham] CHECK CONSTRAINT [FKk5vq8vobu9q3bnlqftjtbhfc6];
GO

ALTER TABLE [dbo].[TaiKhoan]  WITH CHECK ADD  CONSTRAINT [FKb9d9f8kt7sp5hffkvee686qxk] FOREIGN KEY([nhanVien_MaNv])
REFERENCES [dbo].[NhanVien] ([MaNv]);
GO
ALTER TABLE [dbo].[TaiKhoan] CHECK CONSTRAINT [FKb9d9f8kt7sp5hffkvee686qxk];
GO

USE [master];
GO
ALTER DATABASE [QuanLySieuThi] SET READ_WRITE;
GO