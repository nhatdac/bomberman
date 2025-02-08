# Đây là phiên bản game Bomber man huyền thoại, đuợc lập trình lại bằng Java Libgdx

Người chơi sẽ điều khiển Bomberman leo lên 50 tầng nhà máy ngầm để lên mặt đất. Mỗi tầng, bạn phải tiêu diệt hết kẻ thù và mở lối ra. Bomberman ban đầu chỉ đặt được một bom với phạm vi nổ một ô, nhưng có thể thu thập vật phẩm tăng sức mạnh để nâng cao khả năng. Chạm kẻ thù hoặc bị nổ sẽ thua, trừ khi có vật phẩm bảo vệ. Bom tự nổ sau 3 giây, nhưng có thể kích hoạt thủ công nếu có Detonator. Bom cũng có thể nổ dây chuyền.

## Enemies

Có tám kẻ thù khác nhau trong nhà máy ngầm, mỗi kẻ có đặc điểm riêng. Chúng di chuyển với nhiều tốc độ khác nhau, trong đó những con quái nhanh có thể vượt qua Bomberman khi chạy hết tốc lực. Mức độ thông minh của chúng cũng khác nhau: một số lang thang vô định, trong khi số khác bám theo bạn khi bạn cố chạy trốn. Ba trong số chúng có thể đi xuyên tường một cách dễ dàng, khiến chúng khó bị tiêu diệt hơn. Tuy nhiên, nếu bạn phá hủy bức tường mà chúng đang ở bên trong, chúng sẽ bị tiêu diệt theo.

| Enemy  | Name     | Points | Speed       | Smart    | Wallpass | Appearance (First - Last) |
|--------|---------|--------|------------|---------|----------|--------------------------|
| 1      | Balloom  | 100    | 2 (Slow)   | 1 (Low) | No       | 01 - 27                  |
| 2      | Oneal    | 200    | 3 (Normal) | 2 (Normal) | No    | 02 - 32                  |
| 3      | Doll     | 400    | 3 (Normal) | 1 (Low) | No       | 03 - 41                  |
| 4      | Minvo    | 800    | 4 (Fast)   | 2 (Normal) | No    | 04 - 44                  |
| 5      | Kondoria | 1000   | 1 (Slowest) | 3 (High) | Yes     | 07 - 50                  |
| 6      | Ovapi    | 2000   | 2 (Slow)   | 2 (Normal) | Yes    | 10 - 50                  |
| 7      | Pass     | 4000   | 4 (Fast)   | 3 (High) | No      | 14 - 50                  |
| 8      | Pontan   | 8000   | 4 (Fast)   | 3 (High) | Yes     | 48 - 50                  |

## Vật phẩm tăng sức mạnh
Có tám loại vật phẩm tăng sức mạnh khác nhau. Trong đó có vài thứ hiếm và xịn, nhưng tốt nhất người nên tìm ra vật phẩm đặc biệt ẩn trong mỗi màn chơi. Bỏ lỡ một vật phẩm có thể khiến Bomberman gặp bất lợi. Nếu bị nổ hoặc chạm vào kẻ thù, bạn sẽ mất tất cả vật phẩm tăng sức mạnh, ngoại trừ "Bombs" và "Flames", vì chúng là vĩnh viễn.


| Vật phẩm  | Tên       | Mô tả |
|-----------|-----------|-------------|
| Bombs     | Bom       | Tăng số lượng bom có thể đặt cùng lúc lên 1; có thể thu thập nhiều lần. **Lưu ý:** Những vật phẩm bom đầu tiên quan trọng hơn những cái sau. |
| Flames    | Lửa       | Tăng phạm vi nổ của bom theo bốn hướng thêm 1 ô vuông; có thể thu thập nhiều lần. **Mẹo:** Tăng phạm vi nổ giúp chiến đấu với kẻ thù từ khoảng cách an toàn. **Cẩn thận:** Tránh đứng trên đường nổ của bom để đảm bảo an toàn. |
| Speed     | Tốc độ    | Tăng tốc độ di chuyển. Chỉ có thể thu thập ở Màn 4. **Mẹo:** Giúp trốn thoát khỏi kẻ truy đuổi dễ dàng hơn. |
| Wallpass  | Xuyên tường | Cho phép đi xuyên qua các bức tường có thể phá hủy (nhưng không phải tường không thể phá hủy), giống như một số kẻ thù như Kondoria, Ovapi, và Pontan. |
| Detonator | Kích nổ   | Cho phép kích nổ quả bom đầu tiên đã đặt (nút B). **Cẩn thận:** Đảm bảo ở vị trí an toàn để tránh bị ảnh hưởng bởi vụ nổ khi kích hoạt bom. |
| Bombpass  | Xuyên bom | Cho phép đi xuyên qua bom. **Mẹo:** Giúp thoát khỏi tình huống bị mắc kẹt bởi bom đặt sai vị trí. |
| Flamepass | Kháng lửa | Miễn nhiễm với vụ nổ. **Mẹo:** Mở rộng chuỗi phản ứng nổ bằng cách đặt bom liên tiếp. |
| Mystery   | Bí ẩn     | Cung cấp trạng thái bất khả xâm phạm tạm thời, không bị tổn hại bởi quái vật hay vụ nổ. **Cẩn thận:** Hiệu ứng không kéo dài lâu, không nên quá phụ thuộc vào nó trong chiến thuật vượt màn. |


## Vật phẩm thưởng

Có sáu vật phẩm thưởng rất khó tìm, mỗi vật phẩm lại khó lấy hơn vật phẩm trước đó. Mỗi màn chơi chỉ có một vật phẩm thưởng cụ thể. Để thu thập chúng, bạn cần biết cách chúng xuất hiện và màn chơi nào chứa chúng.

Xem phần "Sắp xếp màn chơi" trong trang Hướng dẫn để biết vật phẩm thưởng nào xuất hiện trong từng màn chơi.

| Vật phẩm thưởng  | Tên           | Điểm      | Cách xuất hiện |
|------------------|--------------|----------|----------------|
| Bonus Target    | Bonus Target | 10,000   | Mở lối ra và bước qua nó mà không giết bất kỳ kẻ thù nào. |
| Goddess Mask    | Goddess Mask | 20,000   | Sau khi giết tất cả kẻ thù, đi vòng quanh toàn bộ vòng ngoài của màn chơi. |
| Cola Bottle     | Cola Bottle  | 30,000   | Trước khi giết bất kỳ kẻ thù nào, mở lối ra và bước qua nó, sau đó giữ phím điều hướng trung lập theo cùng một hướng trong 15 giây. |
| Famicom         | Famicom      | 500,000  | Sau khi giết tất cả kẻ thù, kích nổ hơn 248 quả bom với phản ứng dây chuyền. **Lưu ý:** Không thể lấy nếu có vật phẩm Detonator. Vẫn có thể lấy nếu có cả Detonator và Flamepass. |
| Nakamoto-san    | Nakamoto-san | 10,000,000 | Giết tất cả kẻ thù mà không phá hủy bất kỳ bức tường nào, một nhiệm vụ gần như bất khả thi. **Lưu ý:** Không thể lấy nếu bất kỳ kẻ thù nào bị kẹt trong tường. |
| Dezeniman-san   | Dezeniman-san | 20,000,000 | Không giết bất kỳ kẻ thù nào, phá hủy tất cả bức tường trong màn chơi rồi kích nổ một quả bom trên lối ra ba lần mà không giết kẻ thù xuất hiện từ lối ra. |
