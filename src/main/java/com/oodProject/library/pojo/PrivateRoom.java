package com.oodProject.library.pojo;

public class PrivateRoom {
    private int roomNumber;
    private boolean isOccupied;

    public PrivateRoom(int roomNumber) {
        this.roomNumber = roomNumber;
        this.isOccupied = false;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public void displayStatus() {
        System.out.println("Room Number: " + roomNumber + ", Occupied: " + isOccupied);
    }
}
}
