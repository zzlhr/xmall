package com.lhrsite.xshop.repository;

import com.lhrsite.xshop.po.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepositroy extends JpaRepository<Message, String> {
}
