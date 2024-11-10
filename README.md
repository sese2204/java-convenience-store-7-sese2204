# java-convenience-store-precourse
# 🏪 편의점 관리 프로그램

## 📝 구현 기능 목록

### 1️⃣ 파일 입출력
- [x] 상품 정보 읽기 (`products.md`)
- [x] 프로모션 정보 읽기 (`promotions.md`)

### 2️⃣ 상품 관리
- [x] 상품 기본 정보 관리 (이름, 가격, 재고)
- [x] 프로모션/일반 재고 분리 관리
- [x] 재고 차감 기능
  - [x] 프로모션 재고 우선 차감
  - [x] 일반 재고 차감

### 3️⃣ 프로모션 관리
- [x] 프로모션 기간 확인
- [x] 프로모션 유형 관리 (2+1, 1+1)
- [ ] 프로모션 적용 가능 여부 확인
  - [x] 기간 유효성 검증
  - [ ] 재고 유효성 검증

### 4️⃣ 장바구니 기능
- [x] 상품 및 수량 장바구니 담기
- [x] 재고 유효성 검증
- [ ] 프로모션 추가 구매 안내 확인

### 5️⃣ 할인 계산
- [x] 프로모션 할인 계산
  - [x] 2+1 할인 계산
  - [x] 1+1 할인 계산
  - [x] 프로모션에 따른 수량 변동 여부 확인
- [ ] 멤버십 할인 계산
  - [ ] 프로모션 미적용 금액 30% 할인
  - [ ] 최대 8,000원 한도 적용

### 6️⃣ 결제 및 영수증
- [ ] 금액 계산
  - [ ] 총 구매액 계산
  - [ ] 할인 금액 계산 (프로모션/멤버십)
  - [ ] 최종 결제 금액 계산
- [ ] 영수증 생성
  - [ ] 구매 상품 내역
  - [ ] 증정 상품 내역
  - [ ] 금액 정보

### 7️⃣ 입력 기능
- [ ] 상품 구매 입력 처리
  - [ ] `[상품명-수량]` 형식 검증
  - [ ] 상품 존재 여부 검증
  - [ ] 수량 유효성 검증
- [ ] 사용자 선택 입력 처리
  - [ ] 프로모션 추가 구매 여부 (Y/N)
  - [ ] 정가 구매 여부 (Y/N)
  - [ ] 멤버십 할인 적용 여부 (Y/N)
  - [ ] 추가 구매 여부 (Y/N)

### 8️⃣ 출력 기능
- [ ] 상품 목록 출력
  - [ ] 상품 정보 (이름/가격/재고/프로모션)
  - [ ] 재고 없음 표시
- [ ] 안내 메시지 출력
  - [ ] 프로모션 추가 구매 안내
  - [ ] 정가 구매 안내
- [ ] 영수증 출력
  - [ ] 구매 상품 내역
  - [ ] 증정 상품 내역
  - [ ] 금액 정보

### 9️⃣ 예외 처리
- [ ] 입력값 예외 처리
  - [ ] 잘못된 형식 입력
  - [ ] 존재하지 않는 상품
  - [ ] 재고 초과 구매
  - [ ] 잘못된 Y/N 입력
