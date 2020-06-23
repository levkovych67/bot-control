package control.center.bot.repo;

import control.center.bot.object.db.TGUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TGUserDao extends JpaRepository<TGUser, Integer> {




}
