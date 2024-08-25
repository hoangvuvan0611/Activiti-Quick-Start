ACTIVITI - JAVA
======

## Một số link tham khảo về Activiti
[Wiki:](https://en.wikipedia.org/wiki/Activiti_(software))

## Tổng quan về BPMN
### Mô hình hóa nghiệp vụ
* Mô hình hóa: là quá trình tạo ra một mô hình(model) để mô tả, giải thích hoặc dự đoán một hệ thống, hiện tượng hoặc quá trình thực tế. Mô hình là một biểu diễn đơn giản hóa, trừu tượng của một hệ thống phức tạp nhằm hiểu rõ hơn về các thành phần, cấu trúc và các mối quan hệ của nó.
* Trong đó, mô hình quá trình nghiệp vụ là một sơ đồ hoặc biểu đồ mô tả các hoạt động, quy trình, chức năng và mối quan hệ giữa chúng trong một tổ chức hoặc doanh nghiệp. Mô hình này cung cấp một cái nhìn tổng quan về các quy trình nghiệp vụ chính và cách chúng tương tác với nhau để hoàn thành các mục tiêu.
* Trong lập trình, việc mô hình quá trình nghiệp vụ giúp diễn tả một hay nhiều khía cạnh của hệ thống, xác định các diễn biến có thể xảy ra của hệ thống giúp phát triển nhanh chóng và chính xác hơn.

### BPMN là gì
* Business Process Model and Notation (BPMN) là chuẩn để mô hình hóa các tiến trình nghiệp vụ bằng cách cung cấp các kí hiệu đồ họa dùng để đặc tả các tiến trình nghiệp vụ trong biểu đồ tiến trình nghiệp vụ (BPD – Business Process Diagram), dựa trên một luồng biểu đồ kỹ thuật giống với biểu đồ hoạt động trong ngôn ngữ mô hình hóa thống nhất (UML- Unified Modeling Language).
* Mục tiêu của BPMN là hỗ trợ quản lý các tiến trình nghiệp vụ, cho cả người dùng kỹ thuật và người dùng doanh nghiệp, bằng cách cung cấp các ký hiệu trực quan cho người dùng doanh nghiệp, chưa có khả năng thể hiện các tiến trình có ngữ nghĩa phức tạp.
* Nói tóm lại:	Mục đích cơ bản của BPMN là để cung cấp một chuẩn ký hiệu có thể đọc hiểu bởi tất các nghiệp vụ các bên liên quan.

### Các thành phần cơ bản trong BPMN
![image](https://github.com/user-attachments/assets/75840185-97b7-4d03-9331-cdd7b50579c0)
* Về cơ bản chúng ta có thể phân loại các biểu tượng sơ đồ BPMN thành 4 nhóm chinhs: flow objects, connecting objects, swimlanes và artifacts.
  - Flow object: Events, activities, gateways
  - Connecting object: Sequence flow, Message flow, association
  - Swim lanes: Pool, lane
  - Artifacts: Data object, group, annotation

### Task trong BPMN
* Task dùng để mô tả 1 hoạt động trong luồng quy trình và ta chỉ sử dụng task khi hoạt động đó không thể chia nhỏ thêm được nữa. Mỗi task chỉ có thể có 1 người thực hiện nên không có trường hợp task nằm giữa 2 pool hoặc 2 lane.
* Các loại task trong BPMN:
  - User task: task do con người thực hiện trên phần mềm (vd: nhân viên ngân hàng kiểm tra thông tin khách hàng trên hệ thống).
  - Manual task: task này được thực thi bởi con người mà không có bất kỳ sự hỗ trợ nào từ phần mềm ứng dụng (vd: Khách hàng tới ngân hàng yêu cầu rút tiền).
  - Service task: task này được thực hiện bởi hệ thống mà không có sự tương tác của con người (vd: hệ thống tìm kiếm thông tin khách hàng được lưu).
  - Send task: task này gửi 1 message tới 1 pool hoặc 1 lane khác (các bên liên quan tới quy trình xử lý).
  - Receive task: task này nhận 1 message tới 1 pool hoặc 1 lane khác (các bên liên quan tới quy trình xử lý).
  - Business rule task: task này cung cấp đầu vào cho business rule engine và lấy ra kết quả do business rule engine cung cấp (vd: Khách hàng muốn gửi tiết kiệm 6 tháng, 1 năm ... thì lãi là bao nhiêu %).
  - Script task: task này biểu diễn một tập lệnh(script: java, python...) được nhúng trực tiếp trong task và được thực thi bởi business process engine(Công cụ xử lý nghiệp vụ), task hoàn thành tập lệnh được hoàn thành.
* Sự khác nhau giữa Service task và Script task:
  - Script task: thực thi trực tiếp đoạn script, mã code được nhúng vào. Khó sử dụng ở bên ngoài
  - Service task: gửi yêu cầu tới một service của hệ thống hoặc service bên ngoài. Được đặt ở service riêng biệt, có thể tái sử dụng cho các task khác.

### Events trong BPMN
* Start event: Biểu diễn sự kiện xảy ra(không cần kích hoạt) dẫn đến khởi tạo một quy trình.
* End event: 


















