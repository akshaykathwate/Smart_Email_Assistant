package com.SmartmailAssistant.dto;

import lombok.Data;

@Data
public class EmailRequest {

        private String originalEmailContent;
        private String TypeOfEmail;

        public String getTypeOfEmail() {
                return TypeOfEmail;
        }

        public void setTypeOfEmail(String typeOfEmail) {
                TypeOfEmail = typeOfEmail;
        }

        public String getOriginalEmailContent() {
                return originalEmailContent;
        }

        public void setOriginalEmailContent(String originalEmailContent) {
                this.originalEmailContent = originalEmailContent;
        }
}
