package gr.balasis.hotel.engine.monitoring.dbstatistics;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;

@Service
public class HibernateStatisticsService {

    @Autowired
    private EntityManager entityManager;

    public void logQueryStatistics() {
        Session session = entityManager.unwrap(Session.class);
        Statistics statistics = session.getSessionFactory().getStatistics();
        System.out.println ("Total queries executed: " + statistics.getQueryExecutionCount() );
//        System.out.println("Cache hit count: " + statistics.getSecondLevelCacheHitCount());
//        System.out.println("Cache miss count: " + statistics.getSecondLevelCacheMissCount());
        statistics.clear();
    }
}