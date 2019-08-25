#### 1.复制keepalive-release.aar 到app/libs ,app下build android中添加（keepalive.aar 在keepalive/mylib）

~~~java
repositories {
        flatDir {
            dirs 'libs'
        }
}
~~~

dependencies中添加

~~~java
  compile(name:'keepalive', ext:'aar')
~~~

#### 2. 在Application中初始化

~~~xml
  DaemonEnv.init(this);
~~~



#### 3.activity中向用户申请开启后台保活开关（可选）

~~~java
DaemonEnv.whiteListMatters(this, "保活服务的持续运行");
~~~



#### 4.开启业务服务（XXXService为任意继承AbsWorkService服务的业务服务，可选）

~~~java
DaemonEnv.startServiceSafelyWithData(MainActivity.this,XXXService.class);
~~~



###### 注：若需要更改代码，重新生成keepalive-release.aar 方式 Terminal下 gradlew againMakeJar 即可生成到keepalive/mylib

 

 