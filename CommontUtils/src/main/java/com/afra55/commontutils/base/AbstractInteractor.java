package com.afra55.commontutils.base;



import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dmilicic on 8/4/15.
 * <p/>
 * This abstract class implements some common methods for all interactors. Cancelling an interactor, check if its running
 * and finishing an interactor has mostly the same code throughout so that is why this class was created. Field methods
 * are declared volatile as we might use these methods from different threads (mainly from UI).
 * <p/>
 * For example, when an activity is getting destroyed then we should probably cancel an interactor
 * but the request will come from the UI thread unless the request was specifically assigned to a background thread.
 */
public abstract class AbstractInteractor implements Interactor {

    private CompositeSubscription compositeSubscription;

    protected Executor mThreadExecutor;
    protected MainThread mMainThread;

    protected volatile boolean mIsCanceled;
    protected volatile boolean mIsRunning;

    public AbstractInteractor(Executor threadExecutor, MainThread mainThread) {
        mThreadExecutor = threadExecutor;
        mMainThread = mainThread;
    }

    /**
     * This method contains the actual business logic of the interactor. It SHOULD NOT BE USED DIRECTLY but, instead, a
     * developer should call the execute() method of an interactor to make sure the operation is done on a background thread.
     * <p/>
     * This method should only be called directly while doing unit/integration tests. That is the only reason it is declared
     * public as to help with easier testing.
     */
    public abstract void run();

    public void cancel() {
        mIsCanceled = true;
        mIsRunning = false;
        unSubscriber();
    }

    public boolean isRunning() {
        return mIsRunning;
    }

    public void onFinished() {
        onFinished(false);
    }

    /**
     * 如果拦截完成行为，在完成操作时 需要 强制完成
     * @param forced boolean
     */
    public void onFinished(boolean forced) {
        if (forced || !interceptFinishOperation()) {
            mIsRunning = false;
            mIsCanceled = false;
            onCompleted();
        }
    }

    /**
     * 是否拦截完成行为
     * @return true 是， false 否。
     */
    protected boolean interceptFinishOperation() {
        return false;
    }

    public void execute() {

        // mark this interactor as running
        this.mIsRunning = true;

        // start running this interactor in a background thread
        mThreadExecutor.execute(this);
    }

    protected <T> Subscriber<T> addSubscriber(Subscriber<T> subscriber){
        if(compositeSubscription == null){
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscriber);
        return subscriber;
    }

    protected  void unSubscriber(){
        if(compositeSubscription!=null){
            compositeSubscription.unsubscribe();
        }
    }

    protected abstract void onCompleted();

}
