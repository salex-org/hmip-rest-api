package org.salex.hmip.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class HmIPSecurityJournal {
    @JsonProperty("entries")
    private List<HmIPSecurityEvent> entries;

    public List<HmIPSecurityEvent> getEntries() {
        return entries;
    }
}
