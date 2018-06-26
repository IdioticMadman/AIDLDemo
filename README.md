Android跨进程通讯的方式: 
1. 四大组件间传递Bundle;
2. 使用文件共享方式，多进程读写一个相同的文件，获取文件内容进行交互；
3. 使用Messenger，一种轻量级的跨进程通讯方案，底层使用AIDL实现
4. 使用AIDL(Android Interface Definition Language)，Android接口定义语言，用于定义跨进程通讯的接口；
5. 使用ContentProvider，常用于多进程共享数据，比如系统的相册，音乐等，我们也可以通过ContentProvider访问到；
6. 使用Socket传输数据。

这个demo主要是介绍AIDL
### AIDL Demo

### 总结
* 多进程app可以在系统中申请多份内存，但应合理使用，建议把一些高消耗但不常用的模块放到独立的进程，不使用的进程可及时手动关闭；
* 实现多进程的方式有多种：四大组件传递Bundle、Messenger、AIDL等，选择适合自己的使用场景；
* Android中实现多进程通讯，建议使用系统提供的Binder类，该类已经实现了多进程通讯而不需要我们做底层工作；
* 多进程应用，Application将会被创建多次；

#### 构建问题
1. 由于书写aidl文件过程中并不会有提示，而且用界面构建的时候又不会有错误日志的呈现，导致无法定位到错误。所以，我们可以用命令行的方式进行构建。*gradlew build* 尝试构建一下，就可以看到具体的错误定位了。