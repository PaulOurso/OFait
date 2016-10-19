package com.devmobile.ofait.utils.requests;

import com.devmobile.ofait.models.Answer;

/**
 * Created by Tony Wisniewski on 10/10/2016.
 */
public abstract class TaskComplete<TypeData> {

    public Answer<TypeData> result;

    public abstract void run();
}
