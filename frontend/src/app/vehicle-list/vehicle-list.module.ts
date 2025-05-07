export interface Vehicle {
    licensePlate: string;
    activeParking: boolean;
    vehicleType: {
        name: string;
        vehicleType: string;
    };
}

export interface GetVehicleListResult {
    content: Vehicle[];
    page: {
        totalElements: number;
        totalPages: number;
    };
}