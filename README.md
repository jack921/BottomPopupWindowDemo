# BottomPopupWindowView

高仿网易严选底部弹出框

添加引用:

######  Add it in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
###### Add the dependency

```
dependencies {
	 compile 'compile 'com.github.jack921:BottomPopupWindowDemo:v1.0''
}
```



调用方法:

```
<com.jack.bottompopupwindowview.BottomPopupWindowView
    android:layout_width="match_parent"
    android:layout_height="50dp"
    android:background="@android:color/transparent"
    android:layout_alignParentBottom="true">
</com.jack.bottompopupwindowview.BottomPopupWindowView>
```

```
设置内容菜单的View
BottomPopupWindowView.setContextView(bottomView);
设置没有显示菜单时候显示的View(注:bottomView的高度要和BottomPopupWindowView的高度一样，具体看demo)
BottomPopupWindowView.setBaseView(bottomView);
设置动画监听放回的数据，以便根据数据实现动画
BottomPopupWindowView.setAnimatorListener(new AnimatorListener() {
    @Override
    public void startValue(int value) {
        Log.e("value",value+"");            
    }
    @Override
    public void endValue(int value) {
        Log.e("value",value+"");          
    }
});

```

具体请看demo
