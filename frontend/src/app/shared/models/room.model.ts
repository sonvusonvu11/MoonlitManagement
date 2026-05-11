export interface Room {
  roomNumber: number;
  hotelID: number;
  hotelName: string;
  typeID: number;
  typeName: string;
  typeDescription: string;
  pricePerNight: number;
  capacity: number;
  status: 'AVAILABLE' | 'BOOKED' | 'MAINTENANCE' | string;
}

export interface RoomType {
  typeID: number;
  name: string;
  description: string;
  pricePerNight: number;
  capacity: number;
}
