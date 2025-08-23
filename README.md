# 📄 3-Tier Architecture 기반 금융 도메인 고가용성 시스템

---

## 1. 프로젝트 소개

본 프로젝트는 **금융 도메인**에서 필수적인 **고가용성(High Availability)** 을 확보하기 위해  
**3-Tier Architecture**를 설계·구축하고, 장애 상황에서도 서비스가 중단되지 않도록 하는 것을 목표로 합니다.

---

## 2. 팀 소개 및 역할

| 이름 | 역할 | GitHub |
| --- | --- | --- |
| **김문석** | **Project Leader** / 인프라 설계·구축 총괄, 장애 전환 구조 설계 | [github url](https://github.com/moonstone0514) |
| **이정이** | BE & FE 개발, 서버 환경 구성 | [github url](https://github.com/2jeong2) |
| **김현수** | 무중단 장애 전환 구조 설계·구축 | [github url](https://github.com/Hyunsoo1998) |
| **박여명** | 네트워크 구성 및 테스트 | [github url](https://github.com/yeomyeoung) |

---

## 3. Why 3-Tier Architecture?

### 📌 장점

1. **확장성**
    - 계층별 독립 확장 가능 → 수평·수직 스케일 아웃 용이
2. **보안**
    - DB 접근을 WAS를 통해서만 허용 (최소 권한, 네트워크 분리)
3. **유지보수**
    - 로직·UI 서버를 집중 배포 → 클라이언트 무설치, 버전 관리 간편
4. **역할분리 & 재사용성**
    - 프레젠테이션·비즈니스·데이터 계층 분리 → 코드 품질 향상, 모듈 재사용 용이

---

## 4. 3-Tier Architecture 구조

**구성 계층**

- **Presentation Tier** : 사용자의 요청을 처리하는 진입 지점
- **Application Tier** : 비즈니스 로직 실행
- **Database Tier** : 데이터 저장·조회 및 무결성 관리

---

### 4.1 Presentation Tier

- **구성요소**
    - **VIP** (192.168.0.100): 클라이언트 진입 가상 IP
    - **Keepalived**: VIP 관리, 장애 시 자동 전환
    - **HAProxy**: 로드밸런서
    - **Nginx**: 정적 리소스 처리, 동적 요청은 WAS로 전달
- **역할**
    - SSL 종료, 트래픽 부하 분산, 내부 계층 보호
- **특징**
    - 캐싱·압축으로 트래픽 절감
    - 로드밸런싱으로 성능 향상

---

### 4.2 Application Tier

- **구성요소**
    - **Tomcat WAS**
    - **Servlet Container**
    - **Business Logic Module**
- **특징**
    - API 처리, 세션 관리, 트랜잭션 관리
    - 데이터와 분리된 로직 변경·배포 가능
    - DB 물리 IP 대신 VIP(192.168.0.200) 사용 → 장애 시 무중단 전환 가능

---

### 4.3 Database Tier

- **구성요소**
    - MySQL Master-Slave (GTID 기반 복제)
    - Keepalived (VIP 자동 전환)
    - Orchestrator (자동 승격 및 복제 재구성)
- **특징**
    - PK, FK, Lock을 통한 데이터 무결성
    - ACID 트랜잭션 보장
    - Slave를 통한 읽기 부하 분산 가능

---

## 5. 금융 도메인에서 HA의 중요성

- **중단 = 금전적 손실**
- **밀리초 단위의 실시간성** 요구
- 장애 사례:
    - 2020 키움증권, 2021 미래에셋증권, 2024 업비트
    - 원인: 서버 증설 부족, BCP 부재, 시스템 과부하 대비 부족
    - 결과: 수십억 원 배상, 고객 신뢰 하락

---

## 6. HA 구현 요소

| 구성 요소 | 설명 |
| --- | --- |
| **Keepalived** | VIP 기반 고가용성 네트워크 장애 대응 |
| **Orchestrator** | DB 장애 시 자동 승격, Topology 시각화 |
| **DB Replication** | GTID 기반 실시간 복제 |
| **VRRP** | Master 장애 시 VIP를 Backup 노드로 이동 |

---

## 7. 시스템 아키텍처

<img width="578" height="601" alt="image" src="https://github.com/user-attachments/assets/9c784e85-94b8-4656-a11c-02f23a66f364" />


### 7.1 Presentation Tier

```
Client → VIP(192.168.0.100) → HAProxy → Nginx

```

- VIP는 Keepalived가 관리
- LB 장애 시 VIP가 Backup LB로 이동

### 7.2 Application Tier
```
Nginx → WAS(Tomcat) → VIP(192.168.0.200) → DB

```

- VIP를 사용해 DB 접근 → Master 장애 시 IP 변경 불필요

### 7.3 Database Tier

```
Master DB ↔ Slave DB (GTID Replication)

```
- Keepalived로 VIP 전환
- Orchestrator로 자동 승격 및 복제 재구성

---

## 8. PoC (Proof of Concept) 테스트

### PoC 1: Load Balancer 장애

**방법**: Master LB 컨테이너 중지

<img width="985" height="392" alt="image 1" src="https://github.com/user-attachments/assets/756dbffd-9c61-4b7f-a060-1c5d507f24d4" />

<br>

<img width="1005" height="406" alt="image 2" src="https://github.com/user-attachments/assets/245f3452-21fd-477b-8bcc-fbb213a8bca4" />





- **결과**: VIP가 Backup LB로 이동, 서비스 무중단 유지
<img width="934" height="289" alt="image 3" src="https://github.com/user-attachments/assets/424462c9-1269-4d4c-9fce-59fa77309e40" />

- **결과**: VIP가 Backup LB로 이동, 서비스 무중단 유지 VRRP 스크립트(check_haproxy)가 성공적으로 실행되어 서비스 정상 상태를 확인합니다.
<img width="934" height="289" alt="image 4" src="https://github.com/user-attachments/assets/0e5c6464-94af-4a67-9b26-def44cf4140a" />

- **결과**: 새로운 Master가 가상 IP(VIP)의 소유권을 네트워크에 알려(Gratuitous ARP) 트래픽을 인계받습니다.
<img width="934" height="289" alt="image 5" src="https://github.com/user-attachments/assets/8ed89513-2940-4011-a4ed-73b924e17e0b" />

- **결과**: 정상 상태의 Master 서버는 자신의 물리 IP와 가상 IP(192.168.0.100)를 모두 소유합니다.
<img width="934" height="289" alt="image 6" src="https://github.com/user-attachments/assets/bf0bbed6-3baf-40af-9834-c8cdacecee62" />

- **결과**: 정상 상태의 Backup 서버는 자신의 물리 IP만 소유한 채 대기합니다.

---

### PoC 2: Master DB 장애

**방법**: Master MySQL 서비스 중지
<img width="803" height="415" alt="image 7" src="https://github.com/user-attachments/assets/3ea9ade8-eac8-4d4e-a061-dc04eab572be" />


**정상 상황을 나타낸 다이어그램 
<img width="807" height="463" alt="image 8" src="https://github.com/user-attachments/assets/b853c4f7-b714-4de9-a250-b3340f9875ff" />


- **결과**:
    1. VIP가 Slave로 이전
    2. Slave가 Master로 승격
    3. WAS가 IP 변경 없이 새로운 Master에 접근
- **문제점**:
    - Master 재가동 시 VIP 자동 복귀 → Split-Brain 위험
    - 복제 자동 복구 미동작
- **해결방안**:
    - Orchestrator로 복제 구조 재구성 및 완전 자동화


### DB 정상 상황

@ MASTER DB
<img width="861" height="299" alt="image 9" src="https://github.com/user-attachments/assets/ca133366-c946-47ac-8423-7bd681f7c187" />

- **Master Running**
<img width="867" height="100" alt="image 10" src="https://github.com/user-attachments/assets/05f247b1-d49f-447d-b9e8-8f1860982a2c" />

- **VIP도 정상 할당**

@ SLAVE DB
<img width="823" height="352" alt="image 11" src="https://github.com/user-attachments/assets/26dfcaee-2f94-4a53-86ba-65cc6af0cd54" />

- **Slave Running**
<img width="822" height="49" alt="image 12" src="https://github.com/user-attachments/assets/bfd97a5e-00c3-4a1d-826d-c75f56a6be54" />

### 장애 상황 ###

@ MASTER DB
<img width="954" height="399" alt="image 13" src="https://github.com/user-attachments/assets/d9608de2-093b-4023-a88d-6d3af567a815" />

- **Master 종료**
<img width="945" height="103" alt="image 14" src="https://github.com/user-attachments/assets/7107f6fe-269f-4634-b1e9-9de4cd9fa789" />

- **더이상 Master에서 VIP 소유 X**

@ SLAVE DB
<img width="737" height="91" alt="image 15" src="https://github.com/user-attachments/assets/57e5ff15-b32c-4083-89fb-65f641a4f57b" />



- **복제 역할을 멈추고 Master 역할 수행중인 Slave**

<img width="725" height="79" alt="image 16" src="https://github.com/user-attachments/assets/217341a6-c1b0-4052-b992-54d370c4fecd" />

- **VIP를 인계받은 Slave**

@ MASTER 다시 살린 상황
<img width="731" height="358" alt="image 17" src="https://github.com/user-attachments/assets/8adcfe8a-7917-4253-b99e-f8bcf6a9d96e" />

- **MMater db가 다시 살아나 VIP를 인계받은 상황**

<img width="721" height="154" alt="image 18" src="https://github.com/user-attachments/assets/76170f46-c070-4e4b-9485-7a83fda70932" />

- **VIP를 잃은 Slave**

---

## 9. 트러블슈팅

| 문제 | 원인 | 해결 |
| --- | --- | --- |
| LB Split-Brain | auth_pass 불일치, 방화벽 차단 | auth_pass 통일, UFW 해제 |
| 정적 파일 소실 | 컨테이너 재시작 시 파일 손실 | 볼륨 마운트 적용 |
| Slave DB 읽기 실패 | repl 계정 권한 불일치 | 계정 재생성 및 REPLICATION 권한 부여 |
| Master 재가동 시 Split-Brain | VIP 복귀 로직 문제 | Orchestrator + 스크립트 활용 예정 |

---

## 10. 회고

- **김문석**  
  예상치 못한 변수로 완전 자동 전환에 한계가 있었음 → 장애 상황을 고려한 설계 필요성 인식  
  HA는 단순히 서버를 여러 대 두는 것이 아니라, 장애 감지·자동 전환·데이터 일관성이 모두 갖춰져야 함을 배움  
  향후 Orchestrator 구조와 HA 시스템 전반에 대해 더 깊이 있게 학습할 계획

- **이정이**  
  3티어 아키텍처를 직접 구현하며 계층 간 역할 분리와 구조적 장점을 체감할 수 있었음  
  고가용성은 단순히 서버를 여러 대 두는 것이 아닌 장애 감지·자동 전환·상태 동기화가 필요하다는 점을 실습으로 배움

- **박여명**  
  3-Tier 기반 이중화를 학습하고 구성하며 실무에서의 고가용성(2중화, 3중화)의 중요성을 느꼈음  
  실 구현을 통해 HA 보장을 위한 실무적인 방법을 고민했다는 점에서 의미 있었음

- **김현수**  
  고가용성에 대한 이론뿐 아니라 실 구현을 통해 실무적인 방법을 고민할 수 있었음  
  2-Tier → 3-Tier → MSA로 이어지는 아키텍처의 흐름을 이해할 수 있었음

---

## 11. 향후 계획

- [ ] Orchestrator와 Keepalived의 완전 연동
- [ ] Split-Brain 방지 스크립트 배포
- [ ] 복제 구조 자동 복구 기능 고도화
- [ ] MSA 환경으로 확장 테스트
