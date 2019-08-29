package com.apps.cloud.justitia.rest.controller.approval.response;

import com.apps.cloud.common.rest.RestResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RestListApprovalsResponse implements RestResponse {

    private List<RestListApprovalDetails> approvals;

    private RestListApprovalsResponse() {
        // REST
    }

    private RestListApprovalsResponse(Builder builder) {
        approvals = builder.approvals;
    }

    public List<RestListApprovalDetails> getApprovals() {
        return approvals;
    }

    public void setApprovals(List<RestListApprovalDetails> approvals) {
        this.approvals = approvals;
    }

    public static final class Builder {

        private List<RestListApprovalDetails> approvals = new ArrayList<>();

        public Builder withApproval(RestListApprovalDetails approval) {
            this.approvals.add(approval);
            return this;
        }

        public Builder withApprovals(Collection<RestListApprovalDetails> approvals) {
            this.approvals.addAll(approvals);
            return this;
        }

        public RestListApprovalsResponse build() {
            return new RestListApprovalsResponse(this);
        }

    }

}
