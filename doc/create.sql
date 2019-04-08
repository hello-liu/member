

-- 角色表
drop table if exists tb_sys_role;
create table tb_sys_role(
  id		serial,
  name		varchar(50),
  flag		varchar (8),
  create_time		timestamp,
  remark		varchar (255)
);

-- 方法表
drop table if exists tb_sys_func;
create table tb_sys_func(
  id		serial,
  pid int,
  name		varchar(50),
  value		varchar(50),
  flag		varchar (8),
  create_time		timestamp,
  remark		varchar (255)
);

-- 用户表
drop table if exists tb_sys_user;
create table tb_sys_user(
  id		serial,
  merchant_id int,
  nickname		varchar(50),
  sex		varchar (10),
  age		int,
  pwd		varchar (60),
  account		varchar (60) unique ,
  phone varchar(20),
  email varchar(50),
  idnumber varchar(20),
  address varchar(255),
  money		int,-- 余额
  integral		int,-- 积分
  head		int,
  flag		varchar (8),
  create_time		timestamp,
  remark		varchar (255)
);

-- 权限配置表
drop table if exists tb_sys_permis;
create table tb_sys_permis(
  id		serial,
  owner_id		int , -- 用户、角色
  permis_id		int , -- 角色、权限
  flag		varchar (8), -- 1-用户/角色 2-用户/权限 3-角色/权限
  create_time		timestamp,
  remark		varchar (255)
);

-- 日志表
drop table if exists tb_sys_log;
create table tb_sys_log(
  id	serial,
  token	varchar(50) ,
  user_ip	varchar(50),
  json	text,
  result text ,
  use_time int,
  method varchar (50),
  flag	varchar (8),
  create_time	timestamp,
  remark	varchar (255)
);

-- 附件信息表
drop table if exists tb_sys_file;
create table tb_sys_file(
  id	serial,
  size	int,
  name	varchar (100),
  class	varchar (20),
  img_type	varchar (20),
  flag	varchar (8),
  create_time	timestamp,
  user_id	int,
  remark	varchar(255)
);

-- 会员表
drop table if exists tb_busi_member;
create table tb_busi_member(
  id		serial,
  merchant_id int,
  nickname		varchar(50),
  sex		varchar (10),
  age		int,
  pwd		varchar (60),
  account		varchar (60) unique , -- 卡号
  phone varchar(20),
  email varchar(50),
  idnumber varchar(20),
  address varchar(255),
  money		int,-- 余额
  integral		int,-- 积分
  head		int,
  flag		varchar (8),
  create_time		timestamp,
  remark		varchar (255)
);

-- 商户信息表
drop table if exists tb_busi_merchant;
create table merchantId(
  id	serial,
  name	varchar(50),
  logo int,
  address	varchar(255),
  address_point	varchar(50),
  phone	varchar(20) ,
  linkman	varchar(20) ,
  describe	varchar (255),
  flag	varchar (8),
  create_time	timestamp,
  remark	varchar (255)

);

-- 流水表
drop table if exists tb_busi_flow;
create table tb_busi_flow(
  id	serial,
  no varchar (20),
  member_id	int,
  operator_id	int,
  merchant_id	int,
  money	int,
  payway varchar(8),-- 1-余额 2-微信 3-支付宝 4-现金 5-刷卡 6-积分
  content varchar (255),
  type	varchar (8), -- 1-充值 2-消费 3-退款 4-积分兑换
  flag	varchar (8),
  create_time	timestamp,
  remark	varchar (255)
);


