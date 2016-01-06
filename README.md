# 项目说明
项目修改自：https://github.com/maxwell-nc/SimpleMySqlAccessFramework
由于项目整体结构发生巨大变化，所以不在原来的项目上面动土，原来的框架经过简单修改也可以实现通用数据库连接。
不过由于原来的代码性能较低，此项目主要把原来的项目的各部分都分离，封装起来，使用异步监听。

**还是需要注意自己导入对应的JDBC驱动**

# 关于测试
整个框架采用JDBC连接数据库，理论支持任何支持JDBC的数据库
目前测试了MySQL和MS SQLServer
.test.JunitTestCase中提供了测试用例是基于MS SQLServer
由于项目改编自https://github.com/maxwell-nc/SimpleMySqlAccessFramework，可以参考这个项目找到MySQL的测试代码（需要修改）

# 测试用的数据库结构
```
mytest(数据库)
   - usertable(表)
      - username varchar(50) primary key
      - pass varchar(50) not null
```
由于MS SQLServer导出的SQL脚本文件不是很通用，所以没有导出。
