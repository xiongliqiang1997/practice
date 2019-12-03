# practice
练习

数据库文件位于项目根目录下，名字：practice.sql

部署时需要修改的地方
    
    1、项目中用到了lombok，由于Idea编译器使用lombok需要lombok插件的支持
       所以需要打开Settings --> Plugins --> 搜索Lombok --> 下载 --> 重启Idea --> 完成
	
    2、application.properties中：

		#端口号
		server.port=80

		#接受文件上传请求时，修改林缓存文件的目录
		spring.servlet.multipart.location=G:\\tempUploadLocation

		#文件上传路径
		upload.image.path=G:\\recieveFile\\

		#项目所在机器的ip和端口
		server.ip=http://192.168.2.145:80

		#redis
		spring.redis.host=
		spring.redis.port=
		spring.redis.password=

		#配置数据库
		spring.datasource.driver-class-name=
		spring.datasource.url=
		spring.datasource.username=
		spring.datasource.password=
