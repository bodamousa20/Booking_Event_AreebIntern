package com.events.booking.web.Requests;

import com.events.booking.web.Enum.Status;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @NoArgsConstructor
    public class ApiResponse {
        Status status ;
        Object data ;


        public ApiResponse(Status status,Object data) {
            this.status = status;
            this.data = data;
        }

    }


