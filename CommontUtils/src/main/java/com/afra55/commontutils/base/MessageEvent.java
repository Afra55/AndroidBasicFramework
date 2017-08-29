package com.afra55.commontutils.base;

/**
 * Created by yangshuai on 2017/8/29.
 * {link http://afra55.github.io}
 * {link https://github.com/greenrobot/EventBus/}
 *
 *  EventBus.getDefault().post(new MessageEvent());
 *
 *  {@code
 *
 * // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
 *      @Subscribe(threadMode = ThreadMode.MAIN)
 *      public void onMessageEvent(MessageEvent event) {
 *          Toast.makeText(getActivity(), event.message, Toast.LENGTH_SHORT).show();
 *      }
 *
 * // This method will be called when a SomeOtherEvent is posted
 *       @Subscribe
 *       public void handleSomethingElse(SomeOtherEvent event) {
 *           doSomethingWith(event);
 *       }
 *  }
 *
 *  POSTING（默认）：如果使用事件处理函数指定了线程模型为POSTING，那么该事件在哪个线程发布出来的，事件处理函数就会在这个线程中运行，也就是说发布事件和接收事件在同一个线程。在线程模型为POSTING的事件处理函数中尽量避免执行耗时操作，因为它会阻塞事件的传递，甚至有可能会引起ANR。
 *  MAIN:事件的处理会在UI线程中执行。事件处理时间不能太长，长了会ANR的。
 *  BACKGROUND：如果事件是在UI线程中发布出来的，那么该事件处理函数就会在新的线程中运行，如果事件本来就是子线程中发布出来的，那么该事件处理函数直接在发布事件的线程中执行。在此事件处理函数中禁止进行UI更新操作。
 *  ASYNC：无论事件在哪个线程发布，该事件处理函数都会在新建的子线程中执行，同样，此事件处理函数中禁止进行UI更新操作。
 */

public class MessageEvent {
    private String type;
    private Object object;

    public MessageEvent(String type, Object object) {
        this.type = type;
        this.object = object;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
