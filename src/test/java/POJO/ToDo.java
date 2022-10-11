package POJO;

public class ToDo {
   private int userId;
   private int id;
   private String title;
   private boolean completed;

   public int getUserId() {
      return userId;
   }

   public void setUserId(int userId) {
      this.userId = userId;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public boolean isCompleted() {
      return completed;
   }

   public void setCompleted(boolean completed) {
      this.completed = completed;
   }

   @Override
   public String toString() {
      return "ToDo{" +
              "userId=" + userId +
              ", id=" + id +
              ", title='" + title + '\'' +
              ", completed=" + completed +
              '}';
   }
}
//{
//        "userId": 1,
//        "id": 2,
//        "title": "quis ut nam facilis et officia qui",
//        "completed": false
//        }
