#### 1.复制keepalive-release.aar 到app/libs ,app下build android中添加（keepalive-release.aar 在keepalive/mylib）

~~~java
repositories {
        flatDir {
            dirs 'libs'
        }
}
~~~

dependencies中添加

~~~java
  compile(name:'keepalive-release', ext:'aar')
~~~

#### 2. manifest中配置application 

~~~xml
android:name="com.sdk.keepbackground.KeepAliveApplication"（或者继承KeepAliveApplication）
~~~



#### 3.activity中开启后台保活开关

~~~java
DaemonEnv.whiteListMatters(this, "保活服务的持续运行");

DaemonEnv.sendStartWorkBroadcast(MainActivity.this);

~~~



#### 4.开启业务服务（XXXService为任意继承AbsWorkService服务的业务服务）

~~~java
DaemonEnv.startServiceSafelyWithData(MainActivity.this,XXXService.class);
~~~



 

 