package com.summer.common.model.andi;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/20 16:00
 */
@Data
@Schema(name = "接口日志实体")
public class AndiInterfaceLog {
  private int id;
  private String interfaceName;
  private String interfaceDesc;
  private String request;
  private String response;
  private int responseCode;
  private String username;
  private String ip;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private int timeTaken;
  private String traceId;
}
