package com.example.ipldashboard.repository;

import com.example.ipldashboard.model.Match;
import com.example.ipldashboard.model.Team;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import java.util.Comparator;
import java.util.List;

@Configuration
public class TeamRepository {
    public TeamRepository(EntityManager em) {
        this.em = em;
    }
    private final EntityManager em;

    public Team getTeamWithLatestMatches(String teamName){

        @SuppressWarnings("unchecked")
        Team team = (Team) em.createQuery("select t from Team t where t.teamName = :teamName")
                .setParameter("teamName", teamName)
                .getSingleResult();
        if(team != null){
            @SuppressWarnings("unchecked")
            List<Match> matchesByTeam = (List<Match>) em.createQuery("select m from Match m where m.team1 = :teamName")
                    .setParameter("teamName", team.getTeamName())
                    .getResultList();

            @SuppressWarnings("unchecked")
            List<Match> matchesByTeam2 = (List<Match>) em.createQuery("select m from Match m where m.team2 = :teamName")
                    .setParameter("teamName", team.getTeamName())
                    .getResultList();

            matchesByTeam.addAll(matchesByTeam2);

            matchesByTeam.sort(Comparator.comparing(Match::getDate).reversed());

            //Paging hardcoded to 5
            team.setLatestMatches(matchesByTeam.subList(0, 4));

            return team;
        } else {
            return null;
        }

    }
}
