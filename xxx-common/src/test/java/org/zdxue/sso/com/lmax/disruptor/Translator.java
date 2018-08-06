package org.zdxue.sso.com.lmax.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;

import java.nio.ByteBuffer;

public class Translator implements EventTranslatorOneArg<LongEvent, ByteBuffer> {

    @Override
    public void translateTo(LongEvent event, long sequence, ByteBuffer data) {
        event.setValue(data.getLong(0));
    }

}
