import EStyleSheet from 'react-native-extended-stylesheet';


export default styles = EStyleSheet.create({
    addTitle: {
        textAlign: 'center',
        fontSize: 25,
        fontWeight: 'bold'
    },
    addType: {
        marginTop: 10,
        padding: 0,
        backgroundColor: '#eff0f1',
        borderRadius: 7,
    },
    line: {
        fontSize: 18,
        marginTop: 10,
    },
    editableLine: {
        fontSize: 18,
        marginTop: 10,
        backgroundColor: '#eff0f1'
    },
    suppressButton: {
        marginTop: 10,
        height: 35,
        width: '100%',
        borderRadius: 7,
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: '#EA4335'
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
    }
});