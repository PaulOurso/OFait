package com.devmobile.ofait.models;

import java.util.List;

/**
 * Created by MicroStop on 19/10/2016.
 */

public class Answer<TypeData> {
    public int status;
    public String code;
    public Message message;
    public List<TypeData> datas;
    public TypeData data;
}
