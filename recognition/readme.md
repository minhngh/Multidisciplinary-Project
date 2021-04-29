### Thực tập Đồ án Đa ngành
***
#### Recognition
1. Môi trường: tạo môi trường mới, sau đó cài đặt các libraries cần thiết với
    ```
        pip install -r requirements.txt
    ```
2. Quá trình recognition:
    * Face detection: sử dụng *MTCCN*, sau đó align (có thể thay thế nếu muốn)
    * Feature extractor: sử dụng *FaceNet*. Từ tập faces đã được crop và align, forward qua FaceNet để lấy được các embeddings có ***128-d***, sau đó lưu lại.
    * Recognition: đưa 1 ảnh vào, crop và align face, rồi extract feature. Load tập embeddings đã trước ở bước trước, xây dựng **index** với độ đo **L2** từ tập embeddings này. Với *1 embedding* mới, thực hiện tìm kiếm trên *index* để lấy ra các embedding gần nó nhất, dựa trên đó để dự đoán *class* (giống tinh thần ***KNN***). 
    * Notes: cách này so với SVM thì nó flexible hơn.
3.  Dữ liệu: dữ liệu ở trong folder ***images***. Các ảnh cùng *class* thì đặt chung folder con.
4.  Thực thi
    * Đầu tiên, chuẩn bị dữ liệu. Sau đó gõ

    ```
        python main.py
    ```


    * Tiếp theo là test, kết quả in ra terminal là 5 nearest neighbors (mặc định). Có thể thay đổi trong file *config.json* 

    ```
        python recognizer.py -i [image-path]
    ```