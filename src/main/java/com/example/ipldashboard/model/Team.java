package com.example.ipldashboard.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String teamName;
    private long totalMatches;
    private long winMatches;
    @Transient
    private List<Match> latestMatches;

    public List<Match> getLatestMatches() {
        return latestMatches;
    }

    public void setLatestMatches(List<Match> latestMatches) {
        this.latestMatches = latestMatches;
    }

    public Team(String teamName, long totalMatches){
        this.teamName = teamName;
        this.totalMatches = totalMatches;
    }

    public Team(String teamName, long totalMatches, long winMatches) {
        this.teamName = teamName;
        this.totalMatches = totalMatches;
        this.winMatches = winMatches;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public long getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(long totalMatches) {
        this.totalMatches = totalMatches;
    }

    public long getWinMatches() {
        return winMatches;
    }

    public Team() {

    }

    public void setWinMatches(long winMatches) {
        this.winMatches = winMatches;
    }
}
