#### 1.复制keepalive.aar 到app/libs ,app下build android中添加（keepalive.aar 在keepalive根目录下）

~~~java
repositories {
        flatDir {
            dirs 'libs'
        }
}
~~~

dependencies中添加

~~~java
  compile(name:'keepalive-x.x.x', ext:'aar')
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
开启业务服务：DaemonEnv.startServiceSafelyWithData(MainActivity.this,XXXService.class);
停止业务服务：DaemonEnv.sendStopWorkBroadcast(Context context)
    
集成该类需要重写几个抽象方法：
   /**
     * 是否 任务完成, 不再需要服务运行?
     @return true 应当启动服务; false 应当停止服务; null 无法判断, 什么也不做.
     */
 @Override
    public Boolean needStartWorkService() {
        return true;
    }


   /**
     * 开启具体业务，实际执行与isWorkRunning方法返回值有关，
     * 当isWorkRunning返回false才会执行该方法
     */    
    @Override
    public void startWork() {

    }

   /**
     * 业务执行完成需要进行的操作
     * 手动停止服务sendStopWorkBroadcast时回调
     */
    @Override
    public void stopWork() {
      
    }

   /**
     * 任务是否正在运行? 由实现者处理
     * @return 任务正在运行, true; 任务当前不在运行, false; 无法判断, 什么也不做, null.
     */
    @Override
    public Boolean isWorkRunning() {
        return mIsRunning;
    }

   /**
     * 绑定远程service 可根据实际业务情况是否绑定自定义binder或者直接返回默认binder
     */
    @Override
    public IBinder onBindService(Intent intent, Void aVoid) {
        return new Messenger(new Handler()).getBinder();
    }

     /**
     * 服务被kill
     */
    @Override
    public void onServiceKilled() {
        
    }
~~~

###### 注：若需要更改代码，重新生成keepalive.aar ，方式： Terminal下执行gradlew againMakeJar 即可生成到keepalive下

 

 
