package com.api.notifications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ChannelFactory {

    private final Map<String,Canal> canalMap = new HashMap<>();

    @Autowired
    public ChannelFactory(List<Canal> channels) {
        for (Canal channel : channels) {
            canalMap.put(channel.getName().toUpperCase(), channel);
        }
    }

    public Canal getChannel(String name) {
        return canalMap.get(name.toUpperCase());
    }
}
