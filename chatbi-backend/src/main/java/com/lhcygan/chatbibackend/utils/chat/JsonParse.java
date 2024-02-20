package com.lhcygan.chatbibackend.utils.chat;


import java.util.List;

public class JsonParse {
    Header header;
    Payload payload;
}


class Header {
    int code;
    int status;
    String sid;
}

class Payload {
    Choices choices;
}

class Choices {
    List<Text> text;
}

class Text {
    String role;
    String content;
}