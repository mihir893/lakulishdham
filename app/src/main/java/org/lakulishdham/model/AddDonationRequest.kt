package org.lakulishdham.model

class AddDonationRequest {
    var plan_id: String? = "0"
    var subscription_id: String? = ""
    var razorpay_paymentId: String? = ""
    var razorpay_orderId: String? = ""
    var razorpay_signature: String? = ""
    var amount: String? = ""
    var transaction_id: String? = ""
    var transaction_date: String? = ""
    var pay_status: String? = ""
}