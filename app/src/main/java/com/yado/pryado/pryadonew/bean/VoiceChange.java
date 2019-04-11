package com.yado.pryado.pryadonew.bean;

public class VoiceChange {
    private String voiceType; //铃声类型

    private int namepos; //铃声所在位置

    public VoiceChange(String voiceType, int namepos) {
        this.voiceType = voiceType;
        this.namepos = namepos;
    }

    public String getVoiceType() {
        return voiceType;
    }

    public int getNamepos() {
        return namepos;
    }
}
