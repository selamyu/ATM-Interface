package atm.inter.com.AtmInterfaceApplication.repository;

import atm.inter.com.AtmInterfaceApplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserIdAndUserPin(String userId, String userPin);

    User findByUserId(String toUserId);
}
