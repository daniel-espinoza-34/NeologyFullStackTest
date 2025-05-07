export interface ResidentInfo {
    licencePlate: string;
    accumulatedTime: string;
    accumulatedRate: number;
    coveredAmount: number;
    amountLeft: number;
    isPending: boolean;
}
export interface ResidentPayment {
    id: number;
    licensePlate: string;
    paymentDate: Date;
    paymentAmount: number;
}

export interface GetResidentLogResponse {
    resident: {
        licencePlate: string;
        accumulatedTime: number;
        accumulatedRate: number;
        coveredAmount: number;
        pendingAmount: number;
    };
    payments: Array<{
        id: number;
        licensePlate: string;
        paymentDate: string;
        paymentAmount: number;
    }>;
}