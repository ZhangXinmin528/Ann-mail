package com.mail.ann.fragment;


import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;
import android.os.Parcelable;

import com.mail.ann.controller.MessageReference;

/**
 * This class is used to run operations that modify UI elements in the UI thread.
 *
 * <p>We are using convenience methods that add a {@link android.os.Message} instance or a
 * {@link Runnable} to the message queue.</p>
 *
 * <p><strong>Note:</strong> If you add a method to this class make sure you don't accidentally
 * perform the operation in the calling thread.</p>
 */
public class MessageListHandler extends Handler {
    private static final int ACTION_FOLDER_LOADING = 1;
    private static final int ACTION_REFRESH_TITLE = 2;
    private static final int ACTION_PROGRESS = 3;
    private static final int ACTION_REMOTE_SEARCH_FINISHED = 4;
    private static final int ACTION_GO_BACK = 5;
    private static final int ACTION_RESTORE_LIST_POSITION = 6;
    private static final int ACTION_OPEN_MESSAGE = 7;

    private WeakReference<MessageListFragment> mFragment;

    public MessageListHandler(MessageListFragment fragment) {
        mFragment = new WeakReference<>(fragment);
    }
    public void folderLoading(long folderId, boolean loading) {
        android.os.Message msg = android.os.Message.obtain(this, ACTION_FOLDER_LOADING,
                (loading) ? 1 : 0, 0, folderId);
        sendMessage(msg);
    }

    public void refreshTitle() {
        android.os.Message msg = android.os.Message.obtain(this, ACTION_REFRESH_TITLE);
        sendMessage(msg);
    }

    public void progress(final boolean progress) {
        android.os.Message msg = android.os.Message.obtain(this, ACTION_PROGRESS,
                (progress) ? 1 : 0, 0);
        sendMessage(msg);
    }

    public void remoteSearchFinished() {
        android.os.Message msg = android.os.Message.obtain(this, ACTION_REMOTE_SEARCH_FINISHED);
        sendMessage(msg);
    }

    public void updateFooter(final String message) {
        post(new Runnable() {
            @Override
            public void run() {
                MessageListFragment fragment = mFragment.get();
                if (fragment != null) {
                    fragment.updateFooter(message);
                }
            }
        });
    }

    public void goBack() {
        android.os.Message msg = android.os.Message.obtain(this, ACTION_GO_BACK);
        sendMessage(msg);
    }

    public void restoreListPosition(Parcelable savedListState) {
        MessageListFragment fragment = mFragment.get();
        if (fragment != null) {
            android.os.Message msg = android.os.Message.obtain(this, ACTION_RESTORE_LIST_POSITION, savedListState);
            sendMessage(msg);
        }
    }

    public void openMessage(MessageReference messageReference) {
        android.os.Message msg = android.os.Message.obtain(this, ACTION_OPEN_MESSAGE,
                messageReference);
        sendMessage(msg);
    }

    @Override
    public void handleMessage(android.os.Message msg) {
        MessageListFragment fragment = mFragment.get();
        if (fragment == null) {
            return;
        }

        // The following messages don't need an attached activity.
        switch (msg.what) {
            case ACTION_REMOTE_SEARCH_FINISHED: {
                fragment.remoteSearchFinished();
                return;
            }
        }

        // Discard messages if the fragment isn't attached to an activity anymore.
        Activity activity = fragment.getActivity();
        if (activity == null) {
            return;
        }

        switch (msg.what) {
            case ACTION_FOLDER_LOADING: {
                long folderId = (Long) msg.obj;
                boolean loading = (msg.arg1 == 1);
                fragment.folderLoading(folderId, loading);
                break;
            }
            case ACTION_REFRESH_TITLE: {
                fragment.updateTitle();
                break;
            }
            case ACTION_PROGRESS: {
                boolean progress = (msg.arg1 == 1);
                fragment.progress(progress);
                break;
            }
            case ACTION_GO_BACK: {
                fragment.goBack();
                break;
            }
            case ACTION_RESTORE_LIST_POSITION: {
                Parcelable savedListState = (Parcelable) msg.obj;
                fragment.restoreListState(savedListState);
                break;
            }
            case ACTION_OPEN_MESSAGE: {
                MessageReference messageReference = (MessageReference) msg.obj;
                fragment.openMessage(messageReference);
                break;
            }
        }
    }
}
