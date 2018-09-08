import EStyleSheet from 'react-native-extended-stylesheet';


export default styles = EStyleSheet.create({
    addTitle: {
        textAlign: 'center',
        fontSize: 25,
        fontWeight: 'bold'
    },
    addName: {
        marginTop: 20,
        fontSize: 18,
        height: 35,
        padding: 7,
        backgroundColor: '#eff0f1',
        borderRadius: 7,
    },
    datePicker: {
        marginTop: 10,
        width: '100%',
        padding: 0,
        backgroundColor: '#eff0f1',
        borderRadius: 7,
    },
    beersBox: {
        marginTop: 10,
        flexDirection: 'row',
        width: '100%',
        height: 'auto',
    },
    beersLeftPart: {
        width: 'auto',
        height: 'auto',
    },
    beersRightPart: {
        flexDirection: 'row',
        width: '100%',
        height: 'auto',
    },
    normals: {
        flex: 1,
    },
    specials: {
        flex: 1,
        marginLeft: 5,
    },
    normalTitle: {
        fontSize: 18,
        fontStyle: 'italic',
        textAlign: 'center',
    },
    specialTitle: {
        fontSize: 18,
        fontStyle: 'italic',
        textAlign: 'center',
        color: '#edaf36',
    },
    validateButton: {
        marginTop: 10,
        height: 35,
        width: '100%',
        borderRadius: 7,
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: '#edaf36'
    },
    validateText: {
        color: 'white',
        fontSize: 18,
        fontWeight: 'bold',
    },
    addBeers: {
        marginTop: 10,
        marginBottom: 10,
        fontSize: 18,
    }
});