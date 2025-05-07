export interface GetVehicleResponse {
    licensePlate: string;
    vehicleType: {
        vehicleType: string,
        name: string;
    },
    activeParking: boolean;
}

export interface ParkingRecord {
    id: number;
    licensePlate: string;
    startTime: Date;
    endTime?: Date;
    duration: string;
    exitFare?: number;
}

export interface ParkingListResponse {
    records: Array<{
        id: number;
        licensePlate: string;
        startTime: string;
        endTime?: string;
        duration: number;
        exitFare?: number;
    }>;
    totalMinutes: number;
    totalRate: number;
}

export interface Resident {
    licencePlate: string;
    accumulatedTime: string;
    accumulatedRate: number;
    coveredAmount: number;
    amountLeft: number;
    isPending: boolean;
}
export interface GetResidentResponse {
    licencePlate: string;
    accumulatedTime: number;
    accumulatedRate: number;
    coveredAmount: number;
    pendingAmount: number;
}