package com.example.ipldashboard.data;

import com.example.ipldashboard.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Component
@Transactional
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final EntityManager em;

    @Autowired
    public JobCompletionNotificationListener(EntityManager em) {
        this.em = em;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            Map<String, Team> teamData = new HashMap<>();

            em.createQuery("select distinct(m.team1), count(*) from Match m group by m.team1", Object[].class)
                .getResultList()
                .stream()
                .map(it -> new Team((String) it[0], (long)it[1]))
                .forEach(team -> teamData.put(((Team) team).getTeamName(), team));

            em.createQuery("select distinct(m.team2), count(*) from Match m group by m.team2", Object[].class)
                .getResultList()
                .forEach(it -> {
                        Team team = teamData.get((String) it[0]);
                        if(team != null){
                            team.setTotalMatches(team.getTotalMatches() + (long) it[1]);
                            teamData.put(team.getTeamName(),team);
                        }
                        else{
                            Team newTeamFound = new Team((String) it[0], (long) it[1]);
                            teamData.put(newTeamFound.getTeamName(), newTeamFound);
                        }
                    }
                );

            em.createQuery("select distinct(m.winner), count(*) from Match m group by m.winner", Object[].class)
                .getResultList()
                .forEach( it -> {
                    Team team = teamData.get((String) it[0]);
                    if(team != null){
                        team.setWinMatches((long) it[1]);
                        teamData.put(team.getTeamName(), team);
                    }
                });

            teamData.values().forEach(em::persist);
            //teamData.values().forEach(team -> System.out.println(team.getTeamName() + " " + team.getTotalMatches() + " " + team.getWinMatches()));

        }
    }
}
